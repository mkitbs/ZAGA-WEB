package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
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
	
	@OneToMany(mappedBy = "culture")
	private List<Variety> varieties;
	
	@OneToMany(mappedBy = "culture")
	private List<Crop> crops;
}
