package org.xabgesagtx.bot.scrapers.bm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@ToString
@EqualsAndHashCode
public class Band {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	
	@Lob
	private String description;
	
	private Date fullDate;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Link> links = new ArrayList<>();
	
	public void setLinks(List<Link> links) {
		if (links != null) {
			this.links = links;
		}
	}
}
