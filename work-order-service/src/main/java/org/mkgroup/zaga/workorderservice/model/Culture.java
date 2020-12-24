package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.CultureDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "culture", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class Culture {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private OrgCon orgCon;
	
	@Enumerated(EnumType.STRING)
	private CultureType type;
	
	@ManyToOne
	private CultureGroup cultureGroup;
	
	private Long erpId;
	
	@OneToMany(mappedBy = "culture")
	private List<Variety> varieties;
	
	@OneToMany(mappedBy = "culture")
	private List<Crop> crops;
	
	public Culture(CultureDTO c) {
		this.name = c.getName();
		if(c.getOrgCon().equals("K")) {
			this.orgCon = OrgCon.CONVENTIONAL;
		}else if(c.getOrgCon().equals("O")) {
			this.orgCon = OrgCon.ORGANIC;
		} else {
			this.orgCon = OrgCon.OTHER;
		}
		this.erpId = c.getErpId();
		switch(c.getType()) {
		case "V":
			this.type = CultureType.FRUIT;
			break;
		case "P":
			this.type = CultureType.VEGETABLE;
			break;
		case "R":
			this.type = CultureType.CROP_FARMING;
			break;
		case "G": 
			this.type = CultureType.VITICULTURE;
			break;
		case "":
			this.type = CultureType.OTHER;
		default: 
			break;
		}
	}
}
