package org.mkgroup.zaga.workorderservice.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.mkgroup.zaga.workorderservice.dto.ATMReportDTO;
import org.mkgroup.zaga.workorderservice.dto.ATMReportResponseDTO;
import org.mkgroup.zaga.workorderservice.dto.AllWorkOrdersResponseDTO;
import org.mkgroup.zaga.workorderservice.dto.DateDTO;
import org.mkgroup.zaga.workorderservice.dto.OperationsTodayDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.UserAuthDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderEmployeeSAP;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorkerStatus;
import org.mkgroup.zaga.workorderservice.repository.CropRepository;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderMachineRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

@Service
public class WorkOrderService {

	private static final Logger log = Logger.getLogger(WorkOrderService.class);

	@Autowired
	WorkOrderRepository workOrderRepo;

	@Autowired
	OperationService operationService;

	@Autowired
	CropService cropService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	WorkerHoursService workerHoursService;

	@Autowired
	MachineService machineService;

	@Autowired
	MaterialService materialService;

	@Autowired
	MachineStateService machineStateService;

	@Autowired
	SpentMaterialService spentMaterialService;

	@Autowired
	WorkOrderWorkerRepository wowRepo;

	@Autowired
	SpentMaterialRepository spentMaterialRepo;

	@Autowired
	WorkOrderMachineRepository womRepo;

	@Autowired
	MaterialRepository materialRepo;

	@Autowired
	OperationRepository operationRepo;

	@Autowired
	CropRepository cropRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	MachineRepository machineRepo;

	@Autowired
	SAP4HanaProxy sap4hana;

	@Autowired
	RestTemplate restTemplate;

	@Value("${sap.services.s4h}")
	String sapS4Hurl;

	@Value("${sap.services.s4h_close}")
	String sapS4Close;

	public SAPResponse addWorkOrder(WorkOrderDTO workOrderDTO, String sapUserId, Long tenantId) throws Exception {

		SAPResponse sapResponse = new SAPResponse();
		log.info("Work order creation started");

		WorkOrder workOrder = new WorkOrder();

		LocalDate startDate = new LocalDate(Integer.parseInt(workOrderDTO.getDate().getYear()),
				Integer.parseInt(workOrderDTO.getDate().getMonth()), Integer.parseInt(workOrderDTO.getDate().getDay()));
		Date startDateToAdd = startDate.toDate();
		workOrder.setDate(startDateToAdd);

		workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
		workOrder.setCreationDate(new Date());
		workOrder.setTreated(0);

		Operation operation = operationService.getOne(workOrderDTO.getOperationId());
		workOrder.setOperation(operation);

		Crop crop = cropService.getOne(workOrderDTO.getCropId());
		workOrder.setCrop(crop);

		User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());

		workOrder.setResponsible(responsible);
		workOrder.setUserCreatedSapId(Long.parseLong(sapUserId));
		workOrder.setTenantId(tenantId);
		workOrder.setNumOfRefOrder(workOrderDTO.getNumOfRefOrder());
		workOrder.setNote(workOrderDTO.getNote());
		workOrder.setOrgUnit("PIKB");
		workOrder = workOrderRepo.save(workOrder);

		UUID workOrderId = workOrder.getId();

		Map<Long, Boolean> fuelsMap = new HashMap<Long, Boolean>();
		for (WorkOrderWorkerDTO wowDTO : workOrderDTO.getWorkers()) {
			WorkOrderWorker wow = new WorkOrderWorker();

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
			if (wowDTO.getFinalState() != null) {
				wow.setFinalState(wowDTO.getFinalState());
			} else {
				wow.setFinalState(-1.0);
			}
			if (wowDTO.getFuel() != null) {
				wow.setFuel(wowDTO.getFuel());
			} else {
				wow.setFuel(-1.0);
			}
			if (wowDTO.getInitialState() != null) {
				wow.setInitialState(wowDTO.getInitialState());
			} else {
				wow.setInitialState(-1.0);
			}
			if (wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
				wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
			} else {
				wow.setSumState(-1.0);
			}
			wow.setWorkOrder(workOrder);
			if (wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
				wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
			} else {
				wow.setWorkPeriod(-1.0);
			}
			if (wowDTO.getOperationOutput() != null) {
				wow.setOperationOutput(wowDTO.getOperationOutput());
			} else {
				wow.setOperationOutput(-1.0);
			}
			wow.setUser(employeeService.getOne(wowDTO.getUser().getUserId()));
			wow.setOperation(operationService.getOne(wowDTO.getOperation().getId()));
			Machine machine = machineService.getOne(UUID.fromString(wowDTO.getMachine().getDbid()));
			wow.setMachine(machine);
			wow.setStatus(WorkOrderWorkerStatus.NOT_STARTED);

			if (!fuelsMap.containsKey(machine.getFuelErpId()) && workOrder.getCrop().getField().getErpId() != 999997
					&& workOrder.getCrop().getField().getErpId() != 999999
					&& machine.getMachineGroupId().getErpId() != 91) {
				fuelsMap.put(machine.getFuelErpId(), true);
			}

			if (!wowDTO.getConnectingMachine().getDbid().equals("-1")) {
				wow.setConnectingMachine(
						machineService.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
			} else {
				wow.setConnectingMachine(null);
			}
			wow = wowRepo.save(wow);
			workOrder.getWorkers().add(wow);
			workOrder = workOrderRepo.save(workOrder);
		}

		for (SpentMaterialDTO m : workOrderDTO.getMaterials()) {
			SpentMaterial material = new SpentMaterial();

			material.setMaterial(materialService.getOne(m.getMaterial().getDbid()));
			material.setQuantity(m.getQuantity());
			// material.setQuantityPerHectar(m.getQuantity() /
			// workOrder.getCrop().getArea());
			if (m.getSpent() != null) {
				material.setSpent(m.getSpent());
				material.setSpentPerHectar(m.getSpent() / workOrder.getCrop().getArea());
			} else {
				material.setSpent(-1.0);
				material.setSpentPerHectar(-1.0);
			}
			material.setWorkOrder(workOrder);
			material = spentMaterialRepo.save(material);
			workOrder.getMaterials().add(material);
			workOrder = workOrderRepo.save(workOrder);
		}

		Iterator it = fuelsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			System.out.println((Long) pair.getKey());
			SpentMaterial sm = new SpentMaterial();
			if ((Long) pair.getKey() != 0) {
				sm.setMaterial(materialRepo.findByErpId((Long) pair.getKey()).get());
				sm.setQuantity(-1.0);
				sm.setSpent(-1.0);
				sm.setSpentPerHectar(-1.0);
				sm.setFuel(true);
				sm.setWorkOrder(workOrder);
				sm = spentMaterialRepo.save(sm);
				workOrder.getMaterials().add(sm);
				workOrder = workOrderRepo.save(workOrder);
			}

			it.remove(); // avoids a ConcurrentModificationException
		}

		WorkOrder wo = getOneW(workOrderId);

		log.info("Work order creation successfuly finished");

		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(wo, "NEW");
		Map<String, String> headerValues = getHeaderValues(wo);
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");

		log.info("Sending work order to SAP started");

		/*
		 * ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies, "Basic " +
		 * authHeader, csrfToken, "XMLHttpRequest", workOrderSAP);
		 */
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", csrfToken);
		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
		headersRestTemplate.set("Cookie", cookies);
		System.out.println("Token:" + csrfToken);
		System.out.println(headersRestTemplate.toString());
		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

		ResponseEntity<Object> response = null;
		try {
			response = restTemplate.exchange(sapS4Hurl, HttpMethod.POST, entity, Object.class);
		} catch (Exception e) {
			workOrderRepo.delete(wo);
		}

		// System.out.println("Rest Template Testing SAP WO: " +
		// response.getBody().toString());

		if (response == null) {
			workOrderRepo.delete(wo);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}

		String oDataString = response.getBody().toString().replace(":", "-");
		String formatted = formatJSON(oDataString);

		Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
		Matcher matcher = pattern.matcher(formatted);
		String status = "";
		if (matcher.find()) {
			status = matcher.group(1);
		}

		if (status.equals("S")) {
			System.out.println("USPESNO");
			// uspesno
			System.out.println("FORMATTED => " + formatted);
			JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
			JsonArray array = convertedObject.get("d").getAsJsonObject().get("WorkOrderToEmployeeNavigation")
					.getAsJsonObject().get("results").getAsJsonArray();
			JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation")
					.getAsJsonObject().get("results").getAsJsonArray();
			System.out.println("WorkOrderToMaterialNavigation =>" + arrayMaterial);
			System.out.println("WorkOrderToEmployeeNavigation => " + array);

			for (int i = 0; i < array.size(); i++) {
				UUID uid = UUID.fromString(array.get(i).getAsJsonObject().get("WebBackendId").getAsString());
				WorkOrderWorker wow = wowRepo.getOne(uid);
				System.out.println("NASAO" + wow.getMachine().getName());
				wow.setErpId(array.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsInt());
				wowRepo.save(wow);
			}

			for (int i = 0; i < arrayMaterial.size(); i++) {
				if (arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString().equals("")) {
					SpentMaterial sm = new SpentMaterial();
					// long id = 10000049;
					// Material material = materialRepo.findByErpId(id).get();
					// Material material =
					// materialRepo.findByErpId(arrayMaterial.get(i).getAsJsonObject().get("MaterialId").getAsLong()).get();
					// sm.setMaterial(material);
					sm.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
					spentMaterialRepo.save(sm);
				} else {
					UUID uid = UUID
							.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
					SpentMaterial spentMat = spentMaterialRepo.getOne(uid);
					spentMat.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
					spentMaterialRepo.save(spentMat);
				}

			}

			log.info("Sending work order to SAP successfuly finished");
			Pattern patternErpId = Pattern.compile("WorkOrderNumber:(.*?),");
			Matcher matcherId = patternErpId.matcher(formatted);
			Long erpId = 1L;
			if (matcherId.find()) {
				erpId = Long.parseLong(matcherId.group(1));
			}
			wo.setErpId(erpId);
			workOrderRepo.save(wo);

			sapResponse.setSuccess(true);
			sapResponse.setErpId(erpId);

			log.info("Insert work order into db");
		} else if (status.equals("E")) {
			System.out.println("ERROR");

			// Fail
			Pattern patternError = Pattern.compile("MessageText:(.*?),");
			Matcher matcherError = patternError.matcher(formatted);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));

			workOrderRepo.delete(wo);

			sapResponse.setSuccess(false);
			sapResponse.setMessage(errors);

			log.info("Insert work order into db failed");

		} else {
			log.info("Status undefined!");
			workOrderRepo.delete(wo);
		}
		return sapResponse;
	}

	public List<AllWorkOrdersResponseDTO> getAll(Long tenantId) {
		List<WorkOrder> workOrders = workOrderRepo.findAllOrderByCreationDate(tenantId);
		List<AllWorkOrdersResponseDTO> workOrdersDTO = new ArrayList<AllWorkOrdersResponseDTO>();

		for (WorkOrder workOrder : workOrders) {
			AllWorkOrdersResponseDTO workOrderDTO = new AllWorkOrdersResponseDTO(workOrder);
			workOrdersDTO.add(workOrderDTO);
		}
		return workOrdersDTO;
	}

	public List<AllWorkOrdersResponseDTO> getMyWorkOrders(Long tenantId, String sapUserId) {
		List<WorkOrder> workOrders = workOrderRepo.findMyOrderByCreationDate(Long.parseLong(sapUserId), tenantId);
		List<AllWorkOrdersResponseDTO> workOrdersDTO = new ArrayList<AllWorkOrdersResponseDTO>();

		for (WorkOrder workOrder : workOrders) {
			if (workOrder.getOrgUnit().equals("PIKB") || workOrder.getOrgUnit().equals("BIPR")) {
				AllWorkOrdersResponseDTO workOrderDTO = new AllWorkOrdersResponseDTO(workOrder);
				workOrdersDTO.add(workOrderDTO);
			}
		}
		return workOrdersDTO;
	}

	public WorkOrderDTO getOne(UUID id) {
		try {
			WorkOrder workOrder = workOrderRepo.getOne(id);
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			return workOrderDTO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public WorkOrderDTO getOneWorkOrder(UUID id) {
		boolean type = true;//zbog polimorfizma
		try {
			WorkOrder workOrder = workOrderRepo.getOne(id);
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder,type);
			return workOrderDTO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public WorkOrder getOneW(UUID id) {
		try {
			WorkOrder workOrder = workOrderRepo.getOne(id);
			return workOrder;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public SAPResponse updateDataWorkOrder(WorkOrder workOrder) throws Exception {
		SAPResponse retVal = new SAPResponse();

		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");

		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");

		log.info("Updating work order with employee to SAP started");

		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", csrfToken);
		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
		headersRestTemplate.set("Cookie", cookies);
		System.out.println("Token:" + csrfToken);
		System.out.println(headersRestTemplate.toString());
		System.out.println("KA SAPU -> " + workOrderSAP.toString());
		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.exchange(sapS4Hurl, HttpMethod.POST, entity, Object.class);

		System.out.println(response);
		if (response == null) {
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		System.out.println("REZZ" + response.getBody());

		String oDataString = response.toString().replace(":", "-");
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
			retVal.setSuccess(true);
			retVal.getMessage().add("Radni nalog uspesno azuriran");
		} else if (status.equals("E")) {
			System.out.println("ERROR");
			retVal.setSuccess(false);
			retVal.getMessage().add("Greska prilikom azuriranja");
		}

		return retVal;
	}

	public SAPResponse updateWorkOrder(WorkOrderDTO workOrderDTO) throws Exception {

		log.info("Work order update started");

		SAPResponse sapResponse = new SAPResponse();

		WorkOrder workOrder = workOrderRepo.getOne(workOrderDTO.getId());

		LocalDate startDate = new LocalDate(Integer.parseInt(workOrderDTO.getDate().getYear()),
				Integer.parseInt(workOrderDTO.getDate().getMonth()), Integer.parseInt(workOrderDTO.getDate().getDay()));
		Date startDateToAdd = startDate.toDate();
		workOrder.setDate(startDateToAdd);

		if (workOrderDTO.getStatus().equals("Novi")) {
			workOrder.setStatus(WorkOrderStatus.NEW);
		} else if (workOrderDTO.getStatus().equals("U radu")) {
			workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
		} else {
			workOrder.setStatus(WorkOrderStatus.CLOSED);
		}

		workOrder.setTreated(Math.round(workOrderDTO.getTreated() * 100.0) / 100.0);

		workOrder.setCreationDate(workOrderDTO.getCreationDate());

		Operation operation = operationService.getOne(workOrderDTO.getOperationId());
		workOrder.setOperation(operation);

		Crop crop = cropService.getOne(workOrderDTO.getCropId());
		workOrder.setCrop(crop);

		User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());

		workOrder.setResponsible(responsible);
		workOrder.setNumOfRefOrder(workOrderDTO.getNumOfRefOrder());
		workOrder.setNote(workOrderDTO.getNote());
		workOrder.setOrgUnit("PIKB");
		
		checkFuelQuantity(workOrder);

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
		System.out.println("Token:" + csrfToken);
		System.out.println(headersRestTemplate.toString());
		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

		System.out.println("TO SAP => " + workOrderSAP);

		ResponseEntity<Object> response = restTemplate.exchange(sapS4Hurl, HttpMethod.POST, entity, Object.class);

		System.out.println(response);
		if (response == null) {
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		System.out.println("REZZ" + response.getBody());

		String oDataString = response.toString().replace(":", "-");
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
			workOrder = workOrderRepo.save(workOrder);
			sapResponse.setSuccess(true);

		} else if (status.equals("E")) {
			System.out.println("ERROR");
			String error = "";
			// Fail
			Pattern patternError = Pattern.compile("MessageText:(.*?),");
			Matcher matcherError = patternError.matcher(formatted);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));
			System.out.println(error);
			log.error("Updating work order to SAP failed. (" + error + ")");
			sapResponse.setErpId(workOrder.getErpId());
			sapResponse.setSuccess(false);
			sapResponse.setMessage(errors);

		}
		return sapResponse;
	}

	public WorkOrder createCopy(WorkOrder workOrder, DateDTO copyDate, String sapUserId, Long tenantId)
			throws Exception {

		SAPResponse sapResponse = new SAPResponse();

		LocalDate newDate = new LocalDate(Integer.parseInt(copyDate.getYear()), Integer.parseInt(copyDate.getMonth()),
				Integer.parseInt(copyDate.getDay()));
		Date date = newDate.toDate();

		WorkOrder copy = new WorkOrder();
		List<WorkOrderWorker> workers = workOrder.getWorkers();
		List<SpentMaterial> materials = workOrder.getMaterials();
		copy.setCreationDate(new Date());
		copy.setResponsible(workOrder.getResponsible());
		copy.setCrop(workOrder.getCrop());
		copy.setOperation(workOrder.getOperation());
		copy.setTreated(0);
		copy.setClosed(false);
		copy.setWorkers(new ArrayList<WorkOrderWorker>());
		copy.setMaterials(new ArrayList<SpentMaterial>());
		copy.setDate(date);
		copy.setStatus(WorkOrderStatus.IN_PROGRESS);
		copy.setUserCreatedSapId(Long.parseLong(sapUserId));
		copy.setTenantId(tenantId);
		copy.setOrgUnit("PIKB");

		copy = workOrderRepo.save(copy);
		UUID workOrderId = copy.getId();

		for (WorkOrderWorker worker : workers) {

			WorkOrderWorker wow = new WorkOrderWorker();
			wow.setWorkOrder(copy);
			wow.setMachine(worker.getMachine());
			wow.setUser(worker.getUser());
			wow.setOperation(worker.getOperation());
			wow.setConnectingMachine(worker.getConnectingMachine());
			wow.setStatus(WorkOrderWorkerStatus.NOT_STARTED);
			wowRepo.save(wow);
			copy.getWorkers().add(wow);
			copy = workOrderRepo.save(copy);
		}

		for (SpentMaterial material : materials) {
			SpentMaterial spentMaterial = new SpentMaterial();
			spentMaterial.setMaterial(material.getMaterial());
			if (material.isFuel()) {
				spentMaterial.setFuel(true);
			}
			spentMaterial.setWorkOrder(copy);

			spentMaterialRepo.save(spentMaterial);
			copy.getMaterials().add(spentMaterial);
			copy = workOrderRepo.save(copy);
		}

		WorkOrder wo = getOneW(workOrderId);
		log.info("Work order creation successfuly finished");
		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(wo, "NEW");

		Map<String, String> headerValues = getHeaderValues(wo);
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");

		log.info("Sending work order to SAP started");
		/*
		 * ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies, "Basic " +
		 * authHeader, csrfToken, "XMLHttpRequest", workOrderSAP);
		 */
		// Testing HTTPS with RestTemplate
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", csrfToken);
		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
		headersRestTemplate.set("Cookie", cookies);

		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.postForEntity(sapS4Hurl, entity, Object.class);

		// System.out.println("Rest Template Testing SAP WO: " +
		// response.getBody().toString());

		if (response == null) {
			workOrderRepo.delete(wo);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		String oDataString = response.getBody().toString().replace(":", "-");
		String formatted = formatJSON(oDataString);
		System.out.println(formatted);
		JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
		JsonArray array = convertedObject.get("d").getAsJsonObject().get("WorkOrderToEmployeeNavigation")
				.getAsJsonObject().get("results").getAsJsonArray();
		JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation")
				.getAsJsonObject().get("results").getAsJsonArray();
		System.out.println("REZ2" + array.toString());
		Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
		Matcher matcher = pattern.matcher(formatted);
		String status = "";
		if (matcher.find()) {
			status = matcher.group(1);
		}

		if (status.equals("S")) {
			System.out.println("USPESNO");
			// uspesno
			for (int i = 0; i < array.size(); i++) {
				UUID uid = UUID.fromString(array.get(i).getAsJsonObject().get("WebBackendId").getAsString());
				WorkOrderWorker wow = wowRepo.getOne(uid);
				System.out.println("NASAO" + wow.getMachine().getName());
				wow.setErpId(array.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsInt());
				wowRepo.save(wow);
			}

			for (int i = 0; i < arrayMaterial.size(); i++) {
				UUID uid = UUID.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
				SpentMaterial spentMat = spentMaterialRepo.getOne(uid);

				spentMat.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
				spentMaterialRepo.save(spentMat);
			}

			log.info("Sending work order to SAP successfuly finished");
			Pattern patternErpId = Pattern.compile("WorkOrderNumber:(.*?),");
			Matcher matcherId = patternErpId.matcher(formatted);
			Long erpId = 1L;
			if (matcherId.find()) {
				erpId = Long.parseLong(matcherId.group(1));
			}
			wo.setErpId(erpId);
			workOrderRepo.save(wo);

			sapResponse.setSuccess(true);
			sapResponse.setErpId(erpId);

			log.info("Insert work order into db");
		} else if (status.equals("E")) {
			System.out.println("ERROR");

			String error = "";
			// Fail
			Pattern patternError = Pattern.compile("MessageText:(.*?),");
			Matcher matcherError = patternError.matcher(formatted);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));
			System.out.println(error);
			log.error("Sending work order to SAP failed. (" + error + ")");

			workOrderRepo.delete(wo);

			sapResponse.setSuccess(false);
			sapResponse.setMessage(errors);

			log.info("Insert work order into db failed");

		}

		return copy;
	}

	public CloseWorkOrderResponse closeWorkOrder(WorkOrderDTO workOrderDTO) throws Exception {
		log.info("Work order closing started");

		updateWorkOrder(workOrderDTO);

		CloseWorkOrderResponse closeWorkOrder = new CloseWorkOrderResponse();

		WorkOrder workOrder = workOrderRepo.getOne(workOrderDTO.getId());
		workOrder.setTreated(Math.round(workOrderDTO.getTreated() * 100.0) / 100.0);
		workOrderRepo.save(workOrder);
		CloseWorkOrderDTO closeWorkORder = new CloseWorkOrderDTO(workOrder);
		if (workOrderDTO.isCancellation()) {
			closeWorkORder.setCancellation("X");
		}
		System.out.println("CLOSE WO => " + closeWorkORder);
		Map<String, String> headerValues = getHeaderValuesClose();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");

		log.info("Sending work order to SAP started");
		/*
		 * ResponseEntity<?> response = sap4hana.closeWorkOrder(cookies, "Basic " +
		 * authHeader, csrfToken, "XMLHttpRequest", closeWorkORder);
		 */
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", csrfToken);
		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
		headersRestTemplate.set("Cookie", cookies);

		HttpEntity entity = new HttpEntity(closeWorkORder, headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.postForEntity(sapS4Close, entity, Object.class);

		if (response == null) {
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		String json = response.getBody().toString();
		System.out.println(json + "AAA");

		boolean flagErr = false;
		Pattern pattern = Pattern.compile("ReturnStatus=(E|S),");
		if (json.contains("ReturnStatus=E")) {
			System.out.println("ASDFASFASFASFASF");
			flagErr = true;
		}
		Matcher matcher = pattern.matcher(json);
		String flag = "";
		if (matcher.matches()) {
			System.out.println("IMA GA");
			flag = matcher.group(1);
		}

		if (flagErr) {

			System.out.println("USAO U ERR");
			Pattern patternMessage = Pattern.compile("MessageText=(.*?),");
			Matcher matcherMessage = patternMessage.matcher(json);

			matcherMessage.results().forEach(mat -> closeWorkOrder.getErrors().add((mat.group(1))));
			closeWorkOrder.setStatus(false);
			return closeWorkOrder;
		} else if (!flagErr) {
			System.out.println("USAO U SUCES");
			closeWorkOrder.setStatus(true);
			// this.updateWorkOrder(workOrderDTO);
			workOrder.setTreated(Math.round(workOrderDTO.getTreated() * 100.0) / 100.0);
			if (workOrderDTO.isCancellation()) {
				workOrder.setStatus(WorkOrderStatus.CANCELLATION);
			} else {
				workOrder.setStatus(WorkOrderStatus.CLOSED);
				workOrder.setClosed(true);
			}
			workOrderRepo.save(workOrder);

			return closeWorkOrder;
		} else {
			throw new Exception("Greska prilikom zatvaranja naloga.");
		}

		// this.updateWorkOrder(workOrderDTO);
		// workOrder.setTreated(workOrderDTO.getTreated());
		// workOrder.setStatus(WorkOrderStatus.CLOSED);
		// workOrder.setClosed(true);
		// workOrderRepo.save(workOrder);
	}

	// @Scheduled(cron = "0 0/5 * * * ?")
	public void syncOrgUnit() {
		System.out.println("Usao u sync novi");
		String filter = "?$filter=(WorkOrderOpenDate eq datetime'2021-01-01T00:00:00' and WorkOrderOpenTime eq '00:00:00') &$expand=WorkOrderToEmployeeNavigation,WorkOrderToMaterialNavigation&$format=json";

		String url = sapS4Hurl.concat(filter);
		System.out.println("URL FOR SYNC => " + url);

		StringBuilder authEncodingString = new StringBuilder().append("LJKOMNENOVIC").append(":")
				.append("LjubKom987");
		// Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(authEncodingString.toString().getBytes());

		ResponseEntity<Object> sapResponse = null;
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		HttpEntity entity = new HttpEntity(headersRestTemplate);

		sapResponse = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

		String oDataString = sapResponse.getBody().toString().replace(":", "-");
		String formatted = formatJSONSync(oDataString);
		formatted = formatted.replaceAll("WorkOrderToReturnNavigation:[a-zA-Z0-9: '\"().,_\\{\\}\\n-]*,", "");
		formatted = formatted.replaceAll("\\s+", "-");
		formatted = formatted.replaceAll(",-", ",");
		formatted = formatted.replaceAll("\\{-", "\\{");
		JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
		JsonArray arrayAll = convertedObject.get("d").getAsJsonObject().get("results").getAsJsonArray();
		for (int i = 0; i < arrayAll.size(); i++) {
			JsonObject json = arrayAll.get(i).getAsJsonObject();
			if (!json.get("OperationId").getAsString().equals("0000")
					|| !json.get("CropId").getAsString().equals("000000")
					|| !json.get("FieldId").getAsString().equals("000000")) {
				System.out.println(i);
				if (i == arrayAll.size() - 1) {
					System.out.println("done");
				}
				workOrderRepo
						.findByErpId(
								Long.parseLong(arrayAll.get(i).getAsJsonObject().get("WorkOrderNumber").getAsString()))
						.ifPresentOrElse(found -> updateSyncOrgUnit(json, found.getId()), () -> createSync(json));
				;

			}
		}

	}

	private void updateSyncOrgUnit(JsonObject json, UUID id) {
		WorkOrder workOrder = workOrderRepo.getOne(id);
		workOrder.setOrgUnit(json.get("OrganisationUnit").getAsString());
		workOrder = workOrderRepo.save(workOrder);
	}

	@Scheduled(cron = "0 0/5 * * * ?")
	public void synch() {
		System.out.println("Usao u sync");
		List<WorkOrderDTO> response = new ArrayList<WorkOrderDTO>();
		String date2send = "";
		String time2send = "";
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date today = Calendar.getInstance().getTime();
			String reportDate = df.format(today);
			String dateToPrintToFile = reportDate;
			File file = new File("src/main/resources/lastDateOfUpdateDB/testDate.txt");
			if (!file.exists()) {
				file.createNewFile();
				date2send = "2021-01-01T00:00:00";
				time2send = "00:00:00";
			} else {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String lastDate = reader.readLine();
				System.out.println("Current line = " + lastDate);
				date2send = lastDate.split(" ")[0] + "T" + lastDate.split(" ")[1];
				time2send = lastDate.split(" ")[1];
				System.out.println(date2send);
				System.out.println(time2send);
				reader.close();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(dateToPrintToFile);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		LocalDateTime date = LocalDateTime.now();
		String time = date.getHour() + ":" + date.getMinute() + ":" + date.getSecond();
		System.out.println(date);
		System.out.println(time);
		String filter = String.format(
				"?$filter=(WorkOrderOpenDate eq datetime'%s' and WorkOrderOpenTime eq '%s') &$expand=WorkOrderToEmployeeNavigation,WorkOrderToMaterialNavigation&$format=json",
				date2send, time2send);
		// String filter = "?$filter=(WorkOrderOpenDate eq datetime'2021-02-20T00:00:00'
		// and WorkOrderOpenTime eq '00:00:00')
		// &$expand=WorkOrderToEmployeeNavigation,WorkOrderToMaterialNavigation&$format=json";

		String url = sapS4Hurl.concat(filter);
		System.out.println("URL FOR SYNC => " + url);

		StringBuilder authEncodingString = new StringBuilder().append("LJKOMNENOVIC").append(":")
				.append("LjubKom987");
		// Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(authEncodingString.toString().getBytes());

		ResponseEntity<Object> sapResponse = null;
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		HttpEntity entity = new HttpEntity(headersRestTemplate);

		sapResponse = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

		String oDataString = sapResponse.getBody().toString().replace(":", "-");
		System.out.println("ODATASTRING = " + oDataString);
		String formatted = formatJSONSync(oDataString);
		formatted = formatted.replaceAll("WorkOrderToReturnNavigation:[a-zA-Z0-9: '\"().,_\\{\\}\\n-]*,", "");
		System.out.println("FORMATTED = " + formatted);
		formatted = formatted.replaceAll("\\s+", "-");
		System.out.println("FORMATTED 1 = " + formatted);
		formatted = formatted.replaceAll(",-", ",");
		System.out.println("FORMATTED 2 = " + formatted);
		formatted = formatted.replaceAll("\\{-", "\\{");
		System.out.println("FORMATTED 3 = " + formatted);
		JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
		JsonArray arrayAll = convertedObject.get("d").getAsJsonObject().get("results").getAsJsonArray();
		for (int i = 0; i < arrayAll.size(); i++) {
			JsonObject json = arrayAll.get(i).getAsJsonObject();
			if (!json.get("OperationId").getAsString().equals("0000")
					|| !json.get("CropId").getAsString().equals("000000")
					|| !json.get("FieldId").getAsString().equals("000000")) {
				workOrderRepo
						.findByErpId(
								Long.parseLong(arrayAll.get(i).getAsJsonObject().get("WorkOrderNumber").getAsString()))
						.ifPresentOrElse(found -> updateSync(json, found.getId()), () -> createSync(json));
				;
			}

		}
		// return response;
	}

	public List<AllWorkOrdersResponseDTO> getAllByStatus(Long tenantId, WorkOrderStatus status) {
		List<WorkOrder> workOrders = workOrderRepo.findWoByStatus(tenantId, status.toString());
		List<AllWorkOrdersResponseDTO> workOrdersDTO = new ArrayList<AllWorkOrdersResponseDTO>();
		for (WorkOrder workOrder : workOrders) {
			if (workOrder.getOrgUnit().equals("PIKB") || workOrder.getOrgUnit().equals("BIPR")) {
				AllWorkOrdersResponseDTO workOrderDTO = new AllWorkOrdersResponseDTO(workOrder);
				workOrdersDTO.add(workOrderDTO);
			}
		}
		return workOrdersDTO;
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

	public String formatJSONSync(String json) {
		json = json.replace("=", ":");
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,':=\".()/_ -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		json = json.replaceAll("<201 [a-zA-Z ]+,", "");
		json = json.replaceAll(",\\[content[-a-zA-Z0-9,\". ;:_()'\\]<>]+", "");
		// json = json.replaceAll(" NoteHeader:[a-zA-Z
		// \\-0-9!_.,%?\\/()\\\\šŠćĆčČđĐŽž]*,", "");
		// json = json.replaceAll(" NoteItem:[a-zA-Z
		// \\-0-9!_.,%?\\/()\\\\šŠćĆčČđĐŽž]*,", "");
		return json;
	}

	public Map<String, String> getHeaderValues(WorkOrder wo) throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder().append("LJKOMNENOVIC").append(":").append("LjubKom987");
		// Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(authEncodingString.toString().getBytes());

		// Testing HTTPS with RestTemplate
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");

		HttpEntity entity = new HttpEntity(headersRestTemplate);

		ResponseEntity<Object> response = null;
		try {
			response = restTemplate.exchange(sapS4Hurl, HttpMethod.GET, entity, Object.class);
		} catch (Exception e) {
			workOrderRepo.delete(wo);
		}

		// ResponseEntity<Object> resp = sap4hana.getCSRFToken("Basic " + authHeader,
		// "Fetch");

		if (response == null) {
			workOrderRepo.delete(wo);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}

		log.info("Getting X-CSRF-Token successfuly finished");

		HttpHeaders headers = response.getHeaders();
		String csrfToken;
		csrfToken = headers.getValuesAsList("x-csrf-token").stream().findFirst().orElse("nema");

		String cookies = headers.getValuesAsList("Set-Cookie").stream().collect(Collectors.joining(";"));

		Map<String, String> results = new HashMap<String, String>();
		results.put("csrf", csrfToken);
		results.put("authHeader", authHeader);
		results.put("cookies", cookies);
		return results;
	}

	public Map<String, String> getHeaderValues() throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder().append("LJKOMNENOVIC").append(":").append("LjubKom987");
		// Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(authEncodingString.toString().getBytes());
		// Testing HTTPS with RestTemplate
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");

		HttpEntity entity = new HttpEntity(headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.exchange(sapS4Close, HttpMethod.GET, entity, Object.class);
		log.info("Getting X-CSRF-Token successfuly finished");

		HttpHeaders headers = response.getHeaders();
		String csrfToken;
		csrfToken = headers.getValuesAsList("x-csrf-token").stream().findFirst().orElse("nema");

		String cookies = headers.getValuesAsList("Set-Cookie").stream().collect(Collectors.joining(";"));

		Map<String, String> results = new HashMap<String, String>();
		results.put("csrf", csrfToken);
		results.put("authHeader", authHeader);
		results.put("cookies", cookies);
		return results;
	}

	public Map<String, String> getHeaderValuesClose() throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder().append("LJKOMNENOVIC").append(":").append("LjubKom987");
		// Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(authEncodingString.toString().getBytes());

		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");

		HttpEntity entity = new HttpEntity(headersRestTemplate);
		ResponseEntity<Object> resp = restTemplate.exchange(sapS4Hurl, HttpMethod.GET, entity, Object.class);
		// ResponseEntity<Object> resp = sap4hana.getCSRFTokenClose("Basic " +
		// authHeader, "Fetch");

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

	public List<WorkOrderDTO> getAllForTractorDriver(Long tenantId) {
		List<WorkOrder> workOrders = workOrderRepo.findAllOrderByCreationDate(tenantId);
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();

		for (WorkOrder workOrder : workOrders) {
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			workOrdersDTO.add(workOrderDTO);
		}
		return workOrdersDTO;
	}

	public void createSync(JsonObject json) {
		WorkOrder workOrder = new WorkOrder();
		System.out.println("USAO U SYNC CREATE");
		String woDateStr = json.get("WorkOrderDate").getAsString();
		System.out.println(woDateStr);
		woDateStr = woDateStr.split("\\(")[1];
		woDateStr = woDateStr.split("\\)")[0];
		Date woDate = new Date(Long.parseLong(woDateStr));
		System.out.println("DATE CREATE = " + woDate);
		workOrder.setDate(woDate);

		String woCreateStr = json.get("WorkOrderOpenDate").getAsString();
		woCreateStr = woCreateStr.split("\\(")[1];
		woCreateStr = woCreateStr.split("\\)")[0];
		Date woCreateDate = new Date(Long.parseLong(woCreateStr));
		workOrder.setCreationDate(woCreateDate);

		switch (json.get("WorkOrderStatus").getAsString()) {
		case "L":
			workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
			break;
		case "Z":
			workOrder.setStatus(WorkOrderStatus.CLOSED);
			break;
		case "O":
			workOrder.setStatus(WorkOrderStatus.NEW);
			break;
		case "D":
			workOrder.setStatus(WorkOrderStatus.CANCELLATION);
			break;
		}

		// workOrder.setTreated(0);
		System.out.println(json.get("OperationId").getAsString() + "OPERATION");
		Operation operation = operationRepo.findByErpId(Long.parseLong(json.get("OperationId").getAsString())).get();
		workOrder.setOperation(operation);

		Crop crop = cropRepo.findByErpId(Long.parseLong(json.get("CropId").getAsString())).get();
		workOrder.setCrop(crop);

		User responsible = userRepo.findByPerNumber(Long.parseLong(json.get("ResponsibleUserNumber").getAsString()))
				.get();
		workOrder.setResponsible(responsible);

		workOrder.setUserCreatedSapId(Long.parseLong(json.get("ReleasedUserNumber").getAsString()));

		workOrder.setTenantId(1L);// zakuucana vrednost, treba izmeeniti
		if(json.get("NoteHeader") != null) {
			String noteHeader = json.get("NoteHeader").getAsString().replaceAll("-", " ");
			noteHeader = noteHeader.replaceAll("%22", "\"");
			workOrder.setNumOfRefOrder(noteHeader.replaceAll("%2C", ","));
		}
		if (json.get("NoteItem") != null) {
			String noteItem = json.get("NoteItem").getAsString().replaceAll("-", " ");
			noteItem = noteItem.replaceAll("%22", "\"");
			workOrder.setNote(noteItem.replaceAll("%2C", ","));
		}
		
		workOrder.setErpId(Long.parseLong(json.get("WorkOrderNumber").getAsString()));
		workOrder.setOrgUnit(json.get("OrganisationUnit").getAsString());

		workOrder = workOrderRepo.save(workOrder);

		UUID workOrderId = workOrder.getId();

		JsonArray jsonWow = json.get("WorkOrderToEmployeeNavigation").getAsJsonObject().get("results").getAsJsonArray();
		if (jsonWow.size() == 0) {
			workOrder.setTreated(0);
		}
		Map<Long, Boolean> fuelsMap = new HashMap<Long, Boolean>();
		for (int i = 0; i < jsonWow.size(); i++) {
			if (!jsonWow.get(i).getAsJsonObject().get("EmployeeId").getAsString().equals("00000000")
					&& !jsonWow.get(i).getAsJsonObject().get("OperationId").getAsString().equals("0000")
					&& !jsonWow.get(i).getAsJsonObject().get("MasterMachineId").getAsString().equals("")) {
				WorkOrderWorker wow = new WorkOrderWorker();

				if (jsonWow.get(i).getAsJsonObject().get("OperationOutput").getAsString().equals("0.00000")) {
					wow.setOperationOutput(null);
				} else {
					wow.setOperationOutput(Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("OperationOutput").getAsString()));
				}
				if (jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString().equals("0.00000")
						&& jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString().equals("0.00000")) {
					wow.setNightPeriod(-1.0);
					wow.setDayPeriod(-1.0);
				} else {
					wow.setNightPeriod(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString()));
					wow.setDayPeriod(Double
							.parseDouble(jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString()));
				}
				if (!jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString().equals("0.0")) {
					wow.setFinalState(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString()));
				} else {
					wow.setFinalState(-1.0);
				}

				wow.setFuel(Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("SpentFuel").getAsString()));

				if (!jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString().equals("0.0")) {
					wow.setInitialState(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString()));
				} else {
					wow.setInitialState(-1.0);
				}
				if (!jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString().equals("0.0")
						&& !jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString().equals("0.0")) {
					wow.setSumState(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString())
									- Double.parseDouble(
											jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString()));
				} else {
					wow.setSumState(-1.0);
				}
				wow.setWorkOrder(workOrder);
				if (!jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString().equals("0.00000")
						&& !jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString().equals("0.00000")) {
					wow.setWorkPeriod(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString())
									+ Double.parseDouble(
											jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString()));
				} else {
					wow.setWorkPeriod(-1.0);
				}
				User user = userRepo
						.findByPerNumber(
								Long.parseLong(jsonWow.get(i).getAsJsonObject().get("EmployeeId").getAsString()))
						.orElse(null);
				if (user != null) {
					wow.setUser(user);
				}
				Operation op = operationRepo
						.findByErpId(Long.parseLong(jsonWow.get(i).getAsJsonObject().get("OperationId").getAsString()))
						.orElse(null);
				if (op != null) {
					wow.setOperation(op);
				}
				System.out.println(jsonWow.get(i).getAsJsonObject().get("MasterMachineId").getAsString());
				Machine machine = machineRepo
						.findByErpId(jsonWow.get(i).getAsJsonObject().get("MasterMachineId").getAsString())
						.orElse(null);
				if (machine != null) {
					wow.setMachine(machine);
				}

				wow.setErpId(Integer
						.parseInt(jsonWow.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsString()));

				wow.setStatus(WorkOrderWorkerStatus.NOT_STARTED);

				if (jsonWow.get(i).getAsJsonObject().get("Deleted").getAsString().equals("X")) {
					wow.setDeleted(true);
				} else {
					wow.setDeleted(false);
				}

				if (!fuelsMap.containsKey(machine.getFuelErpId())) {
					fuelsMap.put(machine.getFuelErpId(), true);
				}

				if (!jsonWow.get(i).getAsJsonObject().get("SlaveMachineId").getAsString().equals("")) {
					wow.setConnectingMachine(machineRepo
							.findByErpId(jsonWow.get(i).getAsJsonObject().get("SlaveMachineId").getAsString()).get());
				} else {
					wow.setConnectingMachine(null);
				}

				if (jsonWow.get(i).getAsJsonObject().get("NoOperationOutput").getAsString().equals("X")) {
					wow.setNoOperationOutput(true);
				} else {
					wow.setNoOperationOutput(false);
				}

				if (user != null && op != null && machine != null) {
					wow = wowRepo.save(wow);
					workOrder.getWorkers().add(wow);
					workOrder = workOrderRepo.save(workOrder);
				}
			}

		}

		JsonArray jsonWom = json.get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
		System.out.println(jsonWom.size() + " VELICINA LISTE");
		for (int i = 0; i < jsonWom.size(); i++) {
			if (!jsonWom.get(i).getAsJsonObject().get("MaterialId").getAsString().equals("")) {
				SpentMaterial material = new SpentMaterial();
				System.out.println(jsonWom.get(i).getAsJsonObject().get("WorkOrderNumber").getAsString());
				System.out.println(jsonWom.get(i).getAsJsonObject().get("MaterialId").getAsString());
				Material matr = materialRepo
						.findByErpId(Long.parseLong(jsonWom.get(i).getAsJsonObject().get("MaterialId").getAsString()))
						.get();
				material.setMaterial(matr);
				material.setQuantity(
						Double.parseDouble(jsonWom.get(i).getAsJsonObject().get("PlannedQuantity").getAsString()));
				// material.setQuantityPerHectar(m.getQuantity() /
				// workOrder.getCrop().getArea());

				material.setSpent(
						Double.parseDouble(jsonWom.get(i).getAsJsonObject().get("SpentQuantity").getAsString()));
				material.setSpentPerHectar(0.0);

				if (fuelsMap.containsKey(matr.getErpId())) {
					material.setFuel(true);
				}
				material.setWorkOrder(workOrder);
				System.out.println(
						"ERP MAT " + jsonWom.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsString());
				material.setErpId(Integer
						.parseInt(jsonWom.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsString()));
				if (jsonWom.get(i).getAsJsonObject().get("Deleted").getAsString().equals("X")) {
					material.setDeleted(true);
				} else {
					material.setDeleted(false);
				}
				material = spentMaterialRepo.save(material);
				workOrder.getMaterials().add(material);
				workOrder = workOrderRepo.save(workOrder);
			}

		}

		/*
		 * Iterator it = fuelsMap.entrySet().iterator(); while (it.hasNext()) {
		 * Map.Entry pair = (Map.Entry) it.next(); System.out.println(pair.getKey() +
		 * " = " + pair.getValue()); System.out.println((Long) pair.getKey());
		 * SpentMaterial sm = new SpentMaterial(); if ((Long) pair.getKey() != 0) {
		 * sm.setMaterial(materialRepo.findByErpId((Long) pair.getKey()).get());
		 * sm.setQuantity(-1.0); sm.setSpent(-1.0); sm.setSpentPerHectar(-1.0);
		 * sm.setFuel(true); sm.setWorkOrder(workOrder); sm =
		 * spentMaterialRepo.save(sm); workOrder.getMaterials().add(sm); workOrder =
		 * workOrderRepo.save(workOrder); }
		 * 
		 * it.remove(); // avoids a ConcurrentModificationException }
		 */
	}

	public void updateSync(JsonObject json, UUID id) {
		WorkOrder workOrder = workOrderRepo.getOne(id);
		System.out.println("USAO U SYNC UPDATE");
		String woDateStr = json.get("WorkOrderDate").getAsString();
		woDateStr = woDateStr.split("\\(")[1];
		woDateStr = woDateStr.split("\\)")[0];
		Date woDate = new Date(Long.parseLong(woDateStr));
		System.out.println("DATE = " + woDate);

		/*
		 * String woCreateStr = json.get("WorkOrderOpenDate").getAsString(); woCreateStr
		 * = woCreateStr.split("(")[1]; woCreateStr = woCreateStr.split(")")[0]; Date
		 * woCreateDate = new Date(Long.parseLong(woCreateStr));
		 * workOrder.setCreationDate(woCreateDate);
		 */
		switch (json.get("WorkOrderStatus").getAsString()) {
		case "L":
			workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
			break;
		case "Z":
			workOrder.setStatus(WorkOrderStatus.CLOSED);
			break;
		case "O":
			workOrder.setStatus(WorkOrderStatus.NEW);
			break;
		case "D":
			workOrder.setStatus(WorkOrderStatus.CANCELLATION);
			break;
		}
		workOrder.setDate(woDate);

		// workOrder.setTreated(0);
		Operation operation = operationRepo.findByErpId(Long.parseLong(json.get("OperationId").getAsString())).get();
		workOrder.setOperation(operation);

		Crop crop = cropRepo.findByErpId(Long.parseLong(json.get("CropId").getAsString())).get();
		workOrder.setCrop(crop);

		User responsible = userRepo.findByPerNumber(Long.parseLong(json.get("ResponsibleUserNumber").getAsString()))
				.get();
		workOrder.setResponsible(responsible);

		workOrder.setUserCreatedSapId(Long.parseLong(json.get("ReleasedUserNumber").getAsString()));

		workOrder.setTenantId(1L);// zakuucana vrednost, treba izmeeniti
		System.out.println(json.get("NoteHeader"));
		if(json.get("NoteHeader") != null) {
			String noteHeader = json.get("NoteHeader").getAsString().replaceAll("-", " ");
			noteHeader = noteHeader.replaceAll("%22", "\"");
			workOrder.setNumOfRefOrder(noteHeader.replaceAll("%2C", ","));
		}
		if (json.get("NoteItem") != null) {
			String noteItem = json.get("NoteItem").getAsString().replaceAll("-", " ");
			noteItem = noteItem.replaceAll("%22", "\"");
			workOrder.setNote(noteItem.replaceAll("%2C", ","));
		}
		
		workOrder.setOrgUnit(json.get("OrganisationUnit").getAsString());
		workOrder = workOrderRepo.save(workOrder);

		UUID workOrderId = id;

		JsonArray jsonWow = json.get("WorkOrderToEmployeeNavigation").getAsJsonObject().get("results").getAsJsonArray();
		if (jsonWow.size() == 0) {
			workOrder.setTreated(0);
		}
		Map<Long, Boolean> fuelsMap = new HashMap<Long, Boolean>();
		for (int i = 0; i < jsonWow.size(); i++) {
			if (!jsonWow.get(i).getAsJsonObject().get("EmployeeId").getAsString().equals("00000000")
					&& !jsonWow.get(i).getAsJsonObject().get("OperationId").getAsString().equals("0000")
					&& !jsonWow.get(i).getAsJsonObject().get("MasterMachineId").getAsString().equals("")) {
				int erpId = Integer
						.parseInt(jsonWow.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsString());
				System.out.println("ERP ID = " + erpId);
				WorkOrderWorker worker = wowRepo.findByErpIdAndWorkOrderId(erpId, workOrderId).orElse(null);

				WorkOrderWorker wow = new WorkOrderWorker();
				if (worker != null) {
					wow = worker;
				}

				if (jsonWow.get(i).getAsJsonObject().get("OperationOutput").getAsString().equals("0.00000")) {
					wow.setOperationOutput(null);
				} else {
					wow.setOperationOutput(Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("OperationOutput").getAsString()));
				}
				if (jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString().equals("0.00000")
						&& jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString().equals("0.00000")) {
					wow.setNightPeriod(-1.0);
					wow.setDayPeriod(-1.0);
				} else {
					wow.setNightPeriod(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString()));
					wow.setDayPeriod(Double
							.parseDouble(jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString()));
				}
				if (!jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString().equals("0.0")) {
					wow.setFinalState(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString()));
				} else {
					wow.setFinalState(-1.0);
				}

				wow.setFuel(Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("SpentFuel").getAsString()));

				if (!jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString().equals("0.0")) {
					wow.setInitialState(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString()));
				} else {
					wow.setInitialState(-1.0);
				}
				if (!jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString().equals("0.0")
						&& !jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString().equals("0.0")) {
					wow.setSumState(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("MachineTimeEnd").getAsString())
									- Double.parseDouble(
											jsonWow.get(i).getAsJsonObject().get("MachineTimeStart").getAsString()));
				} else {
					wow.setSumState(-1.0);
				}
				wow.setWorkOrder(workOrder);
				if (!jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString().equals("0.00000")
						&& !jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString().equals("0.00000")) {
					wow.setWorkPeriod(
							Double.parseDouble(jsonWow.get(i).getAsJsonObject().get("WorkNightHours").getAsString())
									+ Double.parseDouble(
											jsonWow.get(i).getAsJsonObject().get("WorkEffectiveHours").getAsString()));
				} else {
					wow.setWorkPeriod(-1.0);
				}

				wow.setErpId(erpId);

				User user = userRepo
						.findByPerNumber(
								Long.parseLong(jsonWow.get(i).getAsJsonObject().get("EmployeeId").getAsString()))
						.orElse(null);
				if (user != null) {
					wow.setUser(user);
				}

				Operation op = operationRepo
						.findByErpId(Long.parseLong(jsonWow.get(i).getAsJsonObject().get("OperationId").getAsString()))
						.orElse(null);
				if (op != null) {
					wow.setOperation(op);
				}

				Machine machine = machineRepo
						.findByErpId(jsonWow.get(i).getAsJsonObject().get("MasterMachineId").getAsString())
						.orElse(null);
				if (machine != null) {
					wow.setMachine(machine);
				}

				wow.setStatus(WorkOrderWorkerStatus.NOT_STARTED);

				if (jsonWow.get(i).getAsJsonObject().get("Deleted").getAsString().equals("X")) {
					wow.setDeleted(true);
				} else {
					wow.setDeleted(false);
				}

				if (!fuelsMap.containsKey(machine.getFuelErpId())) {
					fuelsMap.put(machine.getFuelErpId(), true);
				}

				if (!jsonWow.get(i).getAsJsonObject().get("SlaveMachineId").getAsString().equals("")) {
					wow.setConnectingMachine(machineRepo
							.findByErpId(jsonWow.get(i).getAsJsonObject().get("SlaveMachineId").getAsString()).get());
				} else {
					wow.setConnectingMachine(null);
				}

				if (jsonWow.get(i).getAsJsonObject().get("NoOperationOutput").getAsString().equals("X")) {
					wow.setNoOperationOutput(true);
				} else {
					wow.setNoOperationOutput(false);
				}

				if (user != null && op != null && machine != null) {
					wow = wowRepo.save(wow);
					workOrder.getWorkers().add(wow);
					workOrder = workOrderRepo.save(workOrder);
				}

			}

		}

		JsonArray jsonWom = json.get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();

		for (int i = 0; i < jsonWom.size(); i++) {
			if (!jsonWom.get(i).getAsJsonObject().get("MaterialId").getAsString().equals("")) {
				int erpId = Integer
						.parseInt(jsonWom.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsString());
				SpentMaterial mat = spentMaterialRepo.findByErpAndWorkOrder(erpId, workOrderId).orElse(null);
				SpentMaterial material = new SpentMaterial();
				if (mat != null) {
					material = mat;
				}
				Material matr = materialRepo
						.findByErpId(Long.parseLong(jsonWom.get(i).getAsJsonObject().get("MaterialId").getAsString()))
						.get();
				material.setMaterial(matr);
				material.setQuantity(
						Double.parseDouble(jsonWom.get(i).getAsJsonObject().get("PlannedQuantity").getAsString()));
				// material.setQuantityPerHectar(m.getQuantity() /
				// workOrder.getCrop().getArea());

				material.setSpent(
						Double.parseDouble(jsonWom.get(i).getAsJsonObject().get("SpentQuantity").getAsString()));
				material.setSpentPerHectar(0.0);

				if (jsonWom.get(i).getAsJsonObject().get("Deleted").getAsString().equals("X")) {
					material.setDeleted(true);
				} else {
					material.setDeleted(false);
				}
				if (fuelsMap.containsKey(matr.getErpId())) {
					material.setFuel(true);
				}
				material.setWorkOrder(workOrder);
				material.setErpId(erpId);
				material = spentMaterialRepo.save(material);
				workOrder.getMaterials().add(material);
				workOrder = workOrderRepo.save(workOrder);
			}

		}

	}

	public List<AllWorkOrdersResponseDTO> getMyWoByStatus(Long sapUserId, Long tenantId, WorkOrderStatus status) {
		System.out.println(tenantId + " -> " + sapUserId + " -> " + status);
		List<WorkOrder> workOrders = workOrderRepo.findMyWoByStatus(tenantId, sapUserId, status.toString());
		List<AllWorkOrdersResponseDTO> workOrdersDTO = new ArrayList<AllWorkOrdersResponseDTO>();
		for (WorkOrder workOrder : workOrders) {
			if (workOrder.getOrgUnit().equals("PIKB") || workOrder.getOrgUnit().equals("BIPR")) {
				AllWorkOrdersResponseDTO workOrderDTO = new AllWorkOrdersResponseDTO(workOrder);
				workOrdersDTO.add(workOrderDTO);
			}
		}
		System.out.println(workOrdersDTO);
		return workOrdersDTO;
	}

	public List<OperationsTodayDTO> getOperationsForToday(Long tenantId) {
		List<OperationsTodayDTO> retValues = workOrderRepo.findAllOperationsForToday(tenantId);
		return retValues;
	}

	public List<ATMReportResponseDTO> getDataForATMReport(Long tenantId) {
		List<WorkOrder> workOrders = workOrderRepo.findAllForATMReport(tenantId);
		Map<Operation, List<WorkOrder>> map = workOrders.stream()
				.collect(Collectors.groupingBy(WorkOrder::getOperation));
		List<ATMReportResponseDTO> retValues = new ArrayList<ATMReportResponseDTO>();
		for(Map.Entry<Operation, List<WorkOrder>> entry : map.entrySet()) {
			ATMReportResponseDTO atmReportResponse = new ATMReportResponseDTO();
			atmReportResponse.setAtm(entry.getKey().getName());
			List<ATMReportDTO> atmReports = new ArrayList<ATMReportDTO>();
			for(WorkOrder wo : entry.getValue()) {
				if(wo.getErpId() != null) {
					ATMReportDTO atmReport = new ATMReportDTO(wo);
					atmReports.add(atmReport);
				}
				
			}
			atmReportResponse.setWorkOrders(atmReports);
			retValues.add(atmReportResponse);
		}
		return retValues;
		
	}
	
	public void syncNoOperationOutput() {
		List<WorkOrder> workOrders = workOrderRepo.findAll();
		for(WorkOrder workOrder : workOrders) {
			for(WorkOrderWorker wow : workOrder.getWorkers()) {
				if(workOrder.isNoOperationOutput()) {
					wow.setNoOperationOutput(true);
					wowRepo.save(wow);
				} else {
					wow.setNoOperationOutput(false);
					wowRepo.save(wow);
				}
			}
		}
		System.out.println("DONE SYNC NO OPERATION OUTPUT");
	}
	
	public void syncOperationOutput() {
		List<Long> errors = new ArrayList<Long>();
		List<WorkOrder> workOrders = workOrderRepo.findAll();
		for(WorkOrder workOrder : workOrders) {
			if(!workOrder.getStatus().equals(WorkOrderStatus.CLOSED) 
					&& !workOrder.getStatus().equals(WorkOrderStatus.CANCELLATION)) {
				if(workOrder.getWorkers() != null && workOrder.getWorkers().size() >= 1) {
					for(int i=0; i<workOrder.getWorkers().size(); i++) {
						if(!workOrder.isNoOperationOutput() && workOrder.getTreated() != 0.0) {
							if(!workOrder.getWorkers().get(i).isDeleted() && 
									workOrder.getWorkers().get(i).getFinalState() != -1.0 && 
									!workOrder.getWorkers().get(i).getMachine().getErpId().equals("BEZ-MASINE")) {
								int sumFinalState = wowRepo.sumAllFilanStates(workOrder.getId());
								if(sumFinalState > 0) {
									double sum = (workOrder.getWorkers().get(i).getFinalState() 
											* workOrder.getTreated()) / sumFinalState;
									if(sum % 1 != 0) {
										System.out.println("IMA OSTATAK = " + sum);
										sum = Math.round(sum * 100);
										workOrder.getWorkers().get(i).setOperationOutput(sum / 100);
									} else {
										System.out.println("NEMA OSTATAK = " + sum);
										workOrder.getWorkers().get(i).setOperationOutput(sum);
									}
									wowRepo.save(workOrder.getWorkers().get(i));
									WorkOrder updatedWorkOrder = workOrderRepo.getOne(workOrder.getId());
									String status;
									if(updatedWorkOrder.getStatus().equals(WorkOrderStatus.NEW)) {
										status = "Novi";
									} else if (updatedWorkOrder.getStatus().equals(WorkOrderStatus.IN_PROGRESS)) {
										status = "U radu";
									} else if (updatedWorkOrder.getStatus().equals(WorkOrderStatus.CLOSED)){
										status = "Zatvoren";
									} else {
										status = "Storniran";
									}
									String type = "Sync";
									WorkOrderDTO updatedWorkOrderDTO = new WorkOrderDTO(updatedWorkOrder, 
											status, type);
									try {
										SAPResponse response = this.updateWorkOrder(updatedWorkOrderDTO);
										if(!response.isSuccess()) {
											errors.add(response.getErpId());
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							if (!workOrder.getWorkers().get(i).isDeleted() && 
									workOrder.getWorkers().get(i).getMachine().getErpId().equals("BEZ-MASINE")) {
								workOrder.getWorkers().get(i).setNoOperationOutput(true);
								wowRepo.save(workOrder.getWorkers().get(i));
								WorkOrder updatedWorkOrder = workOrderRepo.getOne(workOrder.getId());
								String status;
								if(updatedWorkOrder.getStatus().equals(WorkOrderStatus.NEW)) {
									status = "Novi";
								} else if (updatedWorkOrder.getStatus().equals(WorkOrderStatus.IN_PROGRESS)) {
									status = "U radu";
								} else if (updatedWorkOrder.getStatus().equals(WorkOrderStatus.CLOSED)){
									status = "Zatvoren";
								} else {
									status = "Storniran";
								}
								String type = "Sync";
								WorkOrderDTO updatedWorkOrderDTO = new WorkOrderDTO(updatedWorkOrder, 
										status, type);
								try {
									SAPResponse response = this.updateWorkOrder(updatedWorkOrderDTO);
									if(!response.isSuccess()) {
										errors.add(response.getErpId());
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}
					}
				}
				
			}
			
		}
		System.out.println("DONE SYNC OPERATION OUTPUT");
		System.out.println("ERRORS ON SYNC OPERATION OUTPUT = " + errors.toString());
	}
	
	public void syncCancellation(List<Long> ids) {
		for(Long id : ids) {
			boolean exist = workOrderRepo.existsByErpId(id);
			if (exist) {
				WorkOrder workOrder = workOrderRepo.findByErpId(id).orElse(null);
				if (workOrder != null) {
					workOrder.setStatus(WorkOrderStatus.CANCELLATION);
					workOrderRepo.save(workOrder);
				}
			}
		}
		System.out.println("DONE SYNC CANCELLATION");
	}
	
	public void checkFuelQuantity(WorkOrder wo) {
		for(SpentMaterial material : wo.getMaterials()) {
			if(material.isFuel() && !material.isDeleted()) {
				double fuelQuantity = 0;
				for(WorkOrderWorker wow : wo.getWorkers()) {
					if(wow.getFuel() != 0 && wow.getFuel() != -1 && !wow.isDeleted()) {
						fuelQuantity += wow.getFuel();
					}
				}
				material.setQuantity(fuelQuantity);
				material.setSpent(fuelQuantity);
				if(wo.getTreated() != 0 && wo.getTreated() != -1) {
					material.setQuantityPerHectar(fuelQuantity / wo.getTreated());
					material.setSpentPerHectar(fuelQuantity / wo.getTreated());	
				}
				spentMaterialRepo.save(material);			
			}
		}
	}

}