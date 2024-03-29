package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumFuelDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumFuelPerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumFuelPerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumStateDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumStatePerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumStatePerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dto.NumOfEmployeesPerOperationDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentHourOfWorkerPerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentHourOfWorkerPerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForEmployeeReportDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForMaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerPerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerReportDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderEmployeeSAP;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorkerStatus;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class WorkOrderWorkerService {

	private static final Logger log = Logger.getLogger(WorkOrderWorkerService.class);

	@Autowired
	WorkOrderWorkerRepository wowRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	OperationRepository operationRepo;

	@Autowired
	MachineRepository machineRepo;

	@Autowired
	WorkOrderRepository workOrderRepo;

	@Autowired
	SAP4HanaProxy sap4hana;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	MaterialRepository materialRepo;

	@Autowired
	SpentMaterialRepository spentMaterialRepo;

	@Value("${sap.services.s4h}")
	String sapS4Hurl;

	public WorkOrderWorker getOne(UUID id) {
		try {
			WorkOrderWorker wow = wowRepo.getOne(id);
			return wow;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteWow(UUID id) {
		WorkOrderWorker wow = wowRepo.getOne(id);
		WorkOrderWorkerDTO wowDTO = new WorkOrderWorkerDTO(wow);
		try {
			if (wow.getMachine().getFuelErpId() != 0 && wow.getWorkOrder().getCrop().getField().getErpId() != 999997) {
				deleteFuel(id, wowDTO);
			}
			wowRepo.deleteWorker(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SAPResponse updateWorkOrder(UUID id, WorkOrderWorkerDTO wowDTO) throws Exception {
		System.out.println(id);

		SAPResponse sapResponse = new SAPResponse();

		WorkOrderWorker wow = wowRepo.getOne(id);
		WorkOrder workOrder = wow.getWorkOrder();
		if (wowDTO.getDayPeriod() != null) {
			wow.setDayPeriod(wowDTO.getDayPeriod());
		} else {
			wow.setDayPeriod(-1.0);
		}
		if (wowDTO.getNightPeriod() != null) {
			wow.setNightPeriod(wowDTO.getNightPeriod());
		} else {
			wow.setNightPeriod(-1.0);
		}
		if (wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getDayPeriod() + wowDTO.getNightPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		if (wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if (wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if (wowDTO.getFinalState() != null && wowDTO.getInitialState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		if (wowDTO.getOperationOutput() != null) {
			if (wow.getOperationOutput() != null && wow.getOperationOutput() != -1.0) {
				workOrder.setTreated(workOrder.getTreated() - wow.getOperationOutput() + wowDTO.getOperationOutput());
				wow.setOperationOutput(wowDTO.getOperationOutput());
			} else {
				workOrder.setTreated(workOrder.getTreated() + wowDTO.getOperationOutput());
				wow.setOperationOutput(wowDTO.getOperationOutput());
			}
		} else {
			if (wow.getOperationOutput() != null && wow.getOperationOutput() != -1.0) {
				workOrder.setTreated(workOrder.getTreated() - wow.getOperationOutput());
			}
			wow.setOperationOutput(null);
		}
		if (wowDTO.isNoOperationOutput()) {
			wow.setNoOperationOutput(true);
		} else {
			wow.setNoOperationOutput(false);
		}
		SpentMaterial sm = new SpentMaterial();
		if (wowDTO.getFuel() != null) {
			if (wow.getMachine().getFuelErpId() != 0 && workOrder.getCrop().getField().getErpId() != 999997
					&& workOrder.getCrop().getField().getErpId() != 999999  && wow.getMachine().getMachineGroupId().getErpId() != 91) {
				// add fuel to spent material
				Material material = materialRepo.findByErpId(wow.getMachine().getFuelErpId()).get();
				sm = spentMaterialRepo.findByWoAndMaterial(workOrder.getId(), material.getId()).orElse(null);
				if (sm != null) {
					if (sm.getQuantity() > 0) {
						if (wow.getFuel() < 0) {
							sm.setQuantity(sm.getQuantity() + wowDTO.getFuel());
							sm.setQuantityPerHectar(sm.getQuantityPerHectar() + wowDTO.getFuel());
							sm.setSpent(sm.getSpent() + wowDTO.getFuel());
							sm.setSpentPerHectar(sm.getSpentPerHectar() + wowDTO.getFuel());
							spentMaterialRepo.save(sm);
						} else {
							System.out.println(sm.getQuantity());
							System.out.println(wowDTO.getFuel());
							System.out.println(wow.getFuel());
							sm.setQuantity(sm.getQuantity() + wowDTO.getFuel() - wow.getFuel());
							sm.setQuantityPerHectar(sm.getQuantityPerHectar() + wowDTO.getFuel() - wow.getFuel());
							sm.setSpent(sm.getSpent() + wowDTO.getFuel() - wow.getFuel());
							sm.setSpentPerHectar(sm.getSpentPerHectar() + wowDTO.getFuel() - wow.getFuel());
							spentMaterialRepo.save(sm);
						}

					} else {
						sm.setQuantity(wowDTO.getFuel());
						sm.setQuantityPerHectar(wowDTO.getFuel());
						sm.setSpent(wowDTO.getFuel());
						sm.setSpentPerHectar(wowDTO.getFuel());
						spentMaterialRepo.save(sm);
					}
				}
			}
			wow.setFuel(wowDTO.getFuel());
		} else {
			wow.setFuel(-1.0);
		}
		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		if (wowDTO.getConnectingMachine().getDbid().equals("-1")) {
			wow.setConnectingMachine(null);
		} else {
			wow.setConnectingMachine(machineRepo.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
		}

		wow.setMachine(machineRepo.getOne(UUID.fromString(wowDTO.getMachine().getDbid())));

		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");

		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");

		log.info("Updating work order with employee to SAP started");
		/*
		 * ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies, "Basic " +
		 * authHeader, csrfToken, "XMLHttpRequest", workOrderSAP);
		 */
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", csrfToken);
		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
		headersRestTemplate.set("Cookie", cookies);

		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.exchange(sapS4Hurl, HttpMethod.POST, entity, Object.class);

		System.out.println(response);
		if (response == null) {
			// wowRepo.delete(wow);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		System.out.println("REZZ" + response.getBody());

		String oDataString = response.getBody().toString().replace(":", "-");
		String formatted = formatJSON(oDataString);

		System.out.println(formatted + "ASASA");
		Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
		Matcher matcher = pattern.matcher(formatted);
		String status = "";
		if (matcher.find()) {
			status = matcher.group(1);
		}

		if (status.equals("S")) {
			System.out.println("USPESNO DODAT");
			wow = wowRepo.save(wow);
			workOrderRepo.save(workOrder);
			sapResponse.setSuccess(true);
		} else if (status.equals("E")) {
			System.out.println("ERROR");
			sm.setQuantity(-1.0);
			sm.setQuantityPerHectar(-1.0);
			sm.setSpent(-1.0);
			sm.setSpentPerHectar(-1.0);
			wow.setFuel(-1.0);
			spentMaterialRepo.save(sm);

			Pattern patternError = Pattern.compile("MessageText:(.*?),");
			Matcher matcherError = patternError.matcher(formatted);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));

			sapResponse.setSuccess(false);
			sapResponse.setMessage(errors);

		}
		return sapResponse;
	}

	public void updateWOWBasicInfo(UUID id, WorkOrderWorkerDTO wowDTO) throws Exception {
		WorkOrderWorker wow = wowRepo.getOne(id);
		WorkOrder workOrder = wow.getWorkOrder();
		if (wowDTO.getMachine().getFuelErpId() != 0
				&& wow.getMachine().getFuelErpId() != wowDTO.getMachine().getFuelErpId()
				&& workOrder.getCrop().getField().getErpId() != 999997
				&& workOrder.getCrop().getField().getErpId() != 999999
				&& wow.getMachine().getMachineGroupId().getErpId() != 91) {
			updateFuel(id, wowDTO);
		}

		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		if (wowDTO.getConnectingMachine().getDbid().equals("-1")) {
			wow.setConnectingMachine(null);
		} else {
			wow.setConnectingMachine(machineRepo.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
		}

		wow.setMachine(machineRepo.getOne(UUID.fromString(wowDTO.getMachine().getDbid())));

		wow = wowRepo.save(wow);

		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");

		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");

		log.info("Updating work order with employee to SAP started");
		/*
		 * ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies, "Basic " +
		 * authHeader, csrfToken, "XMLHttpRequest", workOrderSAP);
		 */
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", csrfToken);
		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
		headersRestTemplate.set("Cookie", cookies);

		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.exchange(sapS4Hurl, HttpMethod.POST, entity, Object.class);

		System.out.println(response);
		if (response == null) {
			wowRepo.delete(wow);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		System.out.println("REZZ" + response.getBody());

		String oDataString = response.getBody().toString().replace(":", "-");
		String formatted = formatJSON(oDataString);

		System.out.println(formatted + "ASASA");
		Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
		Matcher matcher = pattern.matcher(formatted);
		String status = "";
		if (matcher.find()) {
			status = matcher.group(1);
		}

		if (status.equals("S")) {
			System.out.println("USPESNO DODAT");
		} else if (status.equals("E")) {
			System.out.println("ERROR");

			throw new Exception("Greska prilikom komunikacije sa SAP-om.");
		}
	}

	public void addWorker(UUID id, WorkOrderWorkerDTO wowDTO) throws Exception {
		WorkOrder workOrder = workOrderRepo.getOne(id);
		WorkOrderWorker wow = new WorkOrderWorker();
		System.out.println("NIGHT PERIOD = " + wow.getNightPeriod());
		if (wowDTO.getNightPeriod() != null) {
			wow.setNightPeriod(wowDTO.getNightPeriod());
		} else {
			wow.setNightPeriod(-1.0);
		}
		if (wowDTO.getDayPeriod() != null) {
			wow.setDayPeriod(wowDTO.getDayPeriod());
		} else {
			wow.setDayPeriod(-1.0);
		}
		if (wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		Machine machine = machineRepo.getOne(UUID.fromString(wowDTO.getMachine().getDbid()));
		wow.setMachine(machine);
		wow.setStatus(WorkOrderWorkerStatus.NOT_STARTED);
		if (wow.getMachine().getFuelErpId() != 0 && workOrder.getCrop().getField().getErpId() != 999997
				&& workOrder.getCrop().getField().getErpId() != 999999 && wow.getMachine().getMachineGroupId().getErpId() != 91) {
			Material material = materialRepo.findByErpId(wow.getMachine().getFuelErpId()).get();
			SpentMaterial spentMat = spentMaterialRepo.findByWoAndMaterial(workOrder.getId(), material.getId())
					.orElse(null);
			boolean exist;
			if (spentMat != null) {
				exist = true;
			} else {
				exist = false;
			}
			if (!exist) {
				SpentMaterial sm = new SpentMaterial();
				sm.setMaterial(materialRepo.findByErpId(machine.getFuelErpId()).get());
				if (wowDTO.getFuel() != null) {
					sm.setQuantity(wowDTO.getFuel());
					sm.setSpentPerHectar(wowDTO.getFuel());
					sm.setSpent(wowDTO.getFuel());
					sm.setSpentPerHectar(wowDTO.getFuel());
					sm.setWorkOrder(workOrder);
				} else {
					sm.setQuantity(-1.0);
					sm.setSpentPerHectar(-1.0);
					sm.setSpent(-1.0);
					sm.setSpentPerHectar(-1.0);
					sm.setWorkOrder(workOrder);
				}
				sm.setFuel(true);
				sm = spentMaterialRepo.save(sm);
				workOrder.getMaterials().add(sm);
				workOrder = workOrderRepo.save(workOrder);
			} else {
				if (wowDTO.getFuel() != null) {
					spentMat.setQuantity(spentMat.getQuantity() + wowDTO.getFuel());
					spentMat.setQuantityPerHectar(spentMat.getQuantityPerHectar() + wowDTO.getFuel());
					spentMat.setSpent(spentMat.getSpent() + wowDTO.getFuel());
					spentMat.setSpentPerHectar(spentMat.getSpentPerHectar() + wowDTO.getFuel());
				} else {
					spentMat.setQuantity(spentMat.getQuantity());
					spentMat.setQuantityPerHectar(spentMat.getQuantityPerHectar());
					spentMat.setSpent(spentMat.getSpent());
					spentMat.setSpentPerHectar(spentMat.getSpentPerHectar());
				}

				spentMaterialRepo.save(spentMat);
			}
		}

		if (wowDTO.getConnectingMachine().getDbid().equals("-1")) {
			wow.setConnectingMachine(null);
		} else {
			wow.setConnectingMachine(machineRepo.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
		}
		if (wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if (wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if (wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		if (wowDTO.getOperationOutput() != null) {
			wow.setOperationOutput(wowDTO.getOperationOutput());
		} else {
			wow.setOperationOutput(-1.0);
		}
		wow.setWorkOrder(workOrder);
		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));

		wow = wowRepo.save(wow);

		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");

		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");

		log.info("Updating work order with employee to SAP started");
		/*
		 * ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies, "Basic " +
		 * authHeader, csrfToken, "XMLHttpRequest", workOrderSAP);
		 */

		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", csrfToken);
		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
		headersRestTemplate.set("Cookie", cookies);

		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.exchange(sapS4Hurl, HttpMethod.POST, entity, Object.class);

		System.out.println(response);
		if (response == null) {
			wowRepo.delete(wow);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		System.out.println("REZZ" + response.getBody());

		String oDataString = response.getBody().toString().replace(":", "-");
		String formatted = formatJSON(oDataString);
		System.out.println("FORMATED => " + formatted);
		JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
		JsonArray array = convertedObject.get("d").getAsJsonObject().get("WorkOrderToEmployeeNavigation")
				.getAsJsonObject().get("results").getAsJsonArray();
		JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation")
				.getAsJsonObject().get("results").getAsJsonArray();
		System.out.println(formatted + "ASASA");
		Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
		Matcher matcher = pattern.matcher(formatted);
		String status = "";
		if (matcher.find()) {
			status = matcher.group(1);
		}

		if (status.equals("S")) {
			System.out.println("USPESNO DODAT");
			for (int i = 0; i < array.size(); i++) {
				UUID uid = UUID.fromString(array.get(i).getAsJsonObject().get("WebBackendId").getAsString());
				WorkOrderWorker worker = wowRepo.getOne(uid);
				System.out.println("RESPONSE => " + array);

				worker.setErpId(array.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsInt());

				wowRepo.save(worker);
			}

			for (int i = 0; i < arrayMaterial.size(); i++) {
				UUID uid = UUID.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
				SpentMaterial sm = spentMaterialRepo.getOne(uid);
				System.out.println("RESPONSE MATERIAL => " + arrayMaterial);
				sm.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
				spentMaterialRepo.save(sm);
			}
		} else if (status.equals("E")) {
			System.out.println("ERROR");
			throw new Exception("Greska prilikom komunikacije sa SAP-om.");
		}

	}

	public Map<String, String> getHeaderValues() throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder().append("LJKOMNENOVIC").append(":").append("LjubKom987");
		// Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(authEncodingString.toString().getBytes());

		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");

		HttpEntity entity = new HttpEntity(headersRestTemplate);

		ResponseEntity<Object> resp = restTemplate.exchange(sapS4Hurl, HttpMethod.GET, entity, Object.class);

		// ResponseEntity<Object> resp = sap4hana.getCSRFToken("Basic " + authHeader,
		// "Fetch");

		if (resp == null) {
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}

		log.info("Getting X-CSRF-Token successfuly finished");

		HttpHeaders headers = resp.getHeaders();
		String csrfToken;
		csrfToken = headers.getValuesAsList("x-csrf-token").stream().findFirst().orElse("nema");

		String cookies = headers.getValuesAsList("Set-Cookie").stream().collect(Collectors.joining(";"));

		Map<String, String> results = new HashMap<String, String>();
		results.put("csrf", csrfToken);
		results.put("authHeader", authHeader);
		results.put("cookies", cookies);
		return results;
	}

	public String formatJSON(String json) {
		json = json.replace("=", ":");
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,':=\".()/_ -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		json = json.replaceAll("<201 [a-zA-Z ]+,", "");
		json = json.replaceAll(",\\[content[-a-zA-Z0-9,\". ;:_()'\\]<>]+", "");
		json = json.replaceAll(" NoteHeader:[a-zA-Z \\-0-9!_.,%?\\/()\\\\\"šŠćĆčČđĐŽž]*,", "");
		json = json.replaceAll(" NoteItem:[a-zA-Z \\-0-9!_.,%?\\/()\\\\\"šŠćĆčČđĐŽž]*,", "");
		// System.out.println(json);

		return json;

	}

	public List<WorkerReportDTO> getWorkersForReport(Long tenantId) {
		List<WorkOrderWorker> wows = wowRepo.findAllByOrderByWorkerId(tenantId);
		
		List<WorkerReportDTO> retValues = new ArrayList<WorkerReportDTO>();
		for(WorkOrderWorker wow : wows) {
			WorkerReportDTO report = new WorkerReportDTO();
			List<WorkOrderForEmployeeReportDTO> workOrders = workOrderRepo.findAllByEmployee(wow.getUser().getId(), tenantId);
			report.setWorker(wow.getUser().getName());
			report.setWorkOrders(workOrders);
			retValues.add(report);
		}
		return retValues;
	}

	public List<WorkOrderTractorDriverDTO> getWorkOrdersForTractorDriver(UUID workerId, Long tenantId) {
		List<WorkOrderWorker> result = wowRepo.findAllWoWByWorker(workerId, tenantId);
		List<WorkOrderTractorDriverDTO> retValues = new ArrayList<WorkOrderTractorDriverDTO>();
		for (WorkOrderWorker wow : result) {
			if (!wow.getMachine().getErpId().equals("BEZ-MASINE")
					&& !wow.getStatus().equals(WorkOrderWorkerStatus.FINISHED)) {
				if (wow.getStatus().equals(WorkOrderWorkerStatus.STARTED)
						|| wow.getStatus().equals(WorkOrderWorkerStatus.PAUSED)) {
					WorkOrderTractorDriverDTO wowDTO = new WorkOrderTractorDriverDTO(wow, true);
					retValues.add(wowDTO);
				} else {
					WorkOrderTractorDriverDTO wowDTO = new WorkOrderTractorDriverDTO(wow, false);
					retValues.add(wowDTO);
				}

			}
		}
		return retValues;
	}

	public void updateFuel(UUID id, WorkOrderWorkerDTO wowDTO) {
		WorkOrderWorker wow = wowRepo.getOne(id);
		WorkOrder workOrder = wow.getWorkOrder();
		int count = 0;
		for (WorkOrderWorker wow2 : workOrder.getWorkers()) {
			if (wow2.getMachine().getFuelErpId() == wow.getMachine().getFuelErpId()) {
				count++;
			}
		}
		if (count > 1) {
			if (wow.getMachine().getFuelErpId() != 0) {
				Material material = materialRepo.findByErpId(wow.getMachine().getFuelErpId()).get();
				SpentMaterial sm = spentMaterialRepo.findByWoAndMaterial(workOrder.getId(), material.getId()).get();
				sm.setQuantity(sm.getQuantity() - wow.getFuel());
				sm.setQuantityPerHectar(sm.getQuantityPerHectar() - wow.getFuel());
				sm.setSpent(sm.getSpent() - wow.getFuel());
				sm.setSpentPerHectar(sm.getSpentPerHectar() - wow.getFuel());
				sm.setFuel(true);
				spentMaterialRepo.save(sm);
			}

		} else {
			if (wow.getMachine().getFuelErpId() != 0) {
				Material material = materialRepo.findByErpId(wow.getMachine().getFuelErpId()).get();
				SpentMaterial sm = spentMaterialRepo.findByWoAndMaterial(workOrder.getId(), material.getId()).get();
				sm.setDeleted(true);
				spentMaterialRepo.save(sm);
			}

		}
		if (wow.getMachine().getFuelErpId() != wowDTO.getMachine().getFuelErpId()
				&& wowDTO.getMachine().getFuelErpId() != 0) {
			Material material = materialRepo.findByErpId(wowDTO.getMachine().getFuelErpId()).get();
			SpentMaterial spentMat = spentMaterialRepo.findByWoAndMaterial(workOrder.getId(), material.getId())
					.orElse(null);
			if (spentMat == null) {
				Material newMaterial = materialRepo.findByErpId(wowDTO.getMachine().getFuelErpId()).get();
				SpentMaterial sm = new SpentMaterial();
				sm.setMaterial(newMaterial);
				if (wowDTO.getFuel() != null) {
					sm.setQuantity(wowDTO.getFuel());
					sm.setQuantityPerHectar(wowDTO.getFuel());
					sm.setSpent(wowDTO.getFuel());
					sm.setSpentPerHectar(wowDTO.getFuel());
				} else {
					sm.setQuantity(-1.0);
					sm.setQuantityPerHectar(-1.0);
					sm.setSpent(-1.0);
					sm.setSpentPerHectar(-1.0);
				}
				sm.setFuel(true);
				sm.setWorkOrder(workOrder);
				sm = spentMaterialRepo.save(sm);
				workOrder.getMaterials().add(sm);
				workOrder = workOrderRepo.save(workOrder);
			} else {
				if (wowDTO.getFuel() != null) {
					if (spentMat.getQuantity() > 0) {
						spentMat.setQuantity(spentMat.getQuantity() + wowDTO.getFuel());
						spentMat.setQuantityPerHectar(spentMat.getQuantityPerHectar() + wowDTO.getFuel());
						spentMat.setSpent(spentMat.getSpent() + wowDTO.getFuel());
						spentMat.setSpentPerHectar(spentMat.getSpentPerHectar() + wowDTO.getFuel());
						spentMat.setFuel(true);
						spentMaterialRepo.save(spentMat);
					}

				}
			}
		}
	}

	public void deleteFuel(UUID id, WorkOrderWorkerDTO wowDTO) {
		WorkOrderWorker wow = wowRepo.getOne(id);
		WorkOrder workOrder = wow.getWorkOrder();
		int count = 0;
		for (WorkOrderWorker wow2 : workOrder.getWorkers()) {
			if (!wow2.isDeleted()) {
				if (wow2.getMachine().getFuelErpId() == wow.getMachine().getFuelErpId()) {
					count++;
				}
			}
		}
		System.out.println("COUNT = " + count);
		if (count > 1) {
			if (wow.getMachine().getFuelErpId() != 0) {
				Material material = materialRepo.findByErpId(wow.getMachine().getFuelErpId()).get();
				SpentMaterial sm = spentMaterialRepo.findByWoAndMaterial(workOrder.getId(), material.getId()).get();
				sm.setQuantity(sm.getQuantity() - wow.getFuel());
				sm.setQuantityPerHectar(sm.getQuantityPerHectar() - wow.getFuel());
				sm.setSpent(sm.getSpent() - wow.getFuel());
				sm.setSpentPerHectar(sm.getSpentPerHectar() - wow.getFuel());
				spentMaterialRepo.save(sm);
			}
		} else {
			System.out.println("USAO U ELSE");
			if (wow.getMachine().getFuelErpId() != 0) {
				System.out.println("USAO U IF");
				Material material = materialRepo.findByErpId(wow.getMachine().getFuelErpId()).get();
				SpentMaterial sm = spentMaterialRepo.findByWoAndMaterial(workOrder.getId(), material.getId()).get();
				sm.setQuantity(0.0);
				sm.setQuantityPerHectar(0.0);
				sm.setSpent(0.0);
				sm.setSpentPerHectar(0.0);
				// sm.setDeleted(true);
				spentMaterialRepo.save(sm);
			}

		}
	}

	public List<NumOfEmployeesPerOperationDTO> getWorkersPerOperation(Long tenantId) {
		List<NumOfEmployeesPerOperationDTO> retVals = wowRepo.findNumOfOperation(tenantId);
		return retVals;
	}

	public List<SpentHourOfWorkerPerCultureReportDTO> getHourOfWorkerPerCulture(Long tenantId) {
		List<SpentHourOfWorkerPerCultureDTO> spentHours = wowRepo.getHourOfWorkersPerCulture(tenantId);
		List<SpentHourOfWorkerPerCultureReportDTO> retValues = new ArrayList<SpentHourOfWorkerPerCultureReportDTO>();
		SpentHourOfWorkerPerCultureReportDTO spentHour = new SpentHourOfWorkerPerCultureReportDTO();

		if (spentHours.size() > 0) {
			spentHour.setCrop(spentHours.get(0).getCrop());
			spentHour.setCulture(spentHours.get(0).getCulture());
			WorkerPerCultureDTO worker = new WorkerPerCultureDTO(spentHours.get(0));
			List<WorkerPerCultureDTO> workers = new ArrayList<WorkerPerCultureDTO>();
			workers.add(worker);
			spentHour.setWorkers(workers);
			if (spentHours.size() == 1) {
				retValues.add(spentHour);
			}
		}

		for (int i = 0; i < spentHours.size() - 1; i++) {
			List<WorkerPerCultureDTO> workers = new ArrayList<WorkerPerCultureDTO>();
			if (spentHours.get(i).getCrop().equals(spentHours.get(i + 1).getCrop())) {
				spentHour.getWorkers().add(new WorkerPerCultureDTO(spentHours.get(i + 1)));
				if (i + 1 == spentHours.size() - 1) {
					retValues.add(spentHour);
				}
			} else {
				retValues.add(spentHour);
				spentHour = new SpentHourOfWorkerPerCultureReportDTO();
				spentHour.setCrop(spentHours.get(i + 1).getCrop());
				spentHour.setCulture(spentHours.get(i + 1).getCulture());
				WorkerPerCultureDTO worker = new WorkerPerCultureDTO(spentHours.get(i + 1));
				workers.add(worker);
				spentHour.setWorkers(workers);
				if (i + 1 == spentHours.size() - 1) {
					retValues.add(spentHour);
				}
			}

		}
		return retValues;
	}

	public List<MachineSumStatePerCultureReportDTO> getMachineSumStatePerCulture(Long tenantId) {
		List<MachineSumStatePerCultureDTO> machineStates = wowRepo.getMachineSumStatePerCulture(tenantId);
		List<MachineSumStatePerCultureReportDTO> retValues = new ArrayList<MachineSumStatePerCultureReportDTO>();
		MachineSumStatePerCultureReportDTO report = new MachineSumStatePerCultureReportDTO();

		if (machineStates.size() > 0) {
			report.setCrop(machineStates.get(0).getCrop());
			report.setCulture(machineStates.get(0).getCulture());
			MachineSumStateDTO machine = new MachineSumStateDTO(machineStates.get(0));
			List<MachineSumStateDTO> machines = new ArrayList<MachineSumStateDTO>();
			machines.add(machine);
			report.setMachines(machines);
			if (machineStates.size() == 1) {
				retValues.add(report);
			}
		}

		for (int i = 0; i < machineStates.size() - 1; i++) {
			List<MachineSumStateDTO> machines = new ArrayList<MachineSumStateDTO>();
			if (machineStates.get(i).getCrop().equals(machineStates.get(i + 1).getCrop())) {
				report.getMachines().add(new MachineSumStateDTO(machineStates.get(i + 1)));
				if (i + 1 == machineStates.size() - 1) {
					retValues.add(report);
				}
			} else {
				retValues.add(report);
				report = new MachineSumStatePerCultureReportDTO();
				report.setCrop(machineStates.get(i + 1).getCrop());
				report.setCulture(machineStates.get(i + 1).getCulture());
				MachineSumStateDTO machine = new MachineSumStateDTO(machineStates.get(i + 1));
				machines.add(machine);
				report.setMachines(machines);
				if (i + 1 == machineStates.size() - 1) {
					retValues.add(report);
				}
			}

		}
		return retValues;
	}

	public List<MachineSumStatePerCultureDTO> getMachineSumState(Long tenantId) {
		List<MachineSumStatePerCultureDTO> retVals = wowRepo.getMachineSumState(tenantId);
		return retVals;
	}

	public List<MachineSumFuelPerCultureReportDTO> getMachineSumFuelPerCulture(Long tenantId) {
		List<MachineSumFuelPerCultureDTO> machineFuels = wowRepo.getMachineSumFuelPerCultureDTO(tenantId);
		List<MachineSumFuelPerCultureReportDTO> retValues = new ArrayList<MachineSumFuelPerCultureReportDTO>();
		MachineSumFuelPerCultureReportDTO report = new MachineSumFuelPerCultureReportDTO();

		if (machineFuels.size() > 0) {
			report.setCrop(machineFuels.get(0).getCrop());
			report.setCulture(machineFuels.get(0).getCulture());
			MachineSumFuelDTO machine = new MachineSumFuelDTO(machineFuels.get(0));
			List<MachineSumFuelDTO> machines = new ArrayList<MachineSumFuelDTO>();
			machines.add(machine);
			report.setMachines(machines);
			if (machineFuels.size() == 1) {
				retValues.add(report);
			}
		}

		for (int i = 0; i < machineFuels.size() - 1; i++) {
			List<MachineSumFuelDTO> machines = new ArrayList<MachineSumFuelDTO>();
			if (machineFuels.get(i).getCrop().equals(machineFuels.get(i + 1).getCrop())) {
				report.getMachines().add(new MachineSumFuelDTO(machineFuels.get(i + 1)));
				if (i + 1 == machineFuels.size() - 1) {
					retValues.add(report);
				}
			} else {
				retValues.add(report);
				report = new MachineSumFuelPerCultureReportDTO();
				report.setCrop(machineFuels.get(i + 1).getCrop());
				report.setCulture(machineFuels.get(i + 1).getCulture());
				MachineSumFuelDTO machine = new MachineSumFuelDTO(machineFuels.get(i + 1));
				machines.add(machine);
				report.setMachines(machines);
				if (i + 1 == machineFuels.size() - 1) {
					retValues.add(report);
				}
			}

		}
		return retValues;
	}
}
