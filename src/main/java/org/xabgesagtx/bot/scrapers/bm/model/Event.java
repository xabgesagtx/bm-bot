package org.xabgesagtx.bot.scrapers.bm.model;

import static org.xabgesagtx.bot.main.DateUtils.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@ToString
@EqualsAndHashCode
@Table(indexes = @Index(name="CLEAN_ID_IDX", columnList = "cleanId"))
public class Event {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

	private String cleanId;
	
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate date;
    
    private LocalDateTime fullStartDate;
    private LocalDateTime fullEntryDate;
    
    @Lob
    private String title;
    
    @Lob
    private String description;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Band> bands;
    public void setBands(List<Band> bands) {
    	if (bands != null) {
    		this.bands = bands;
    	}
    }
    
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Link> links = new ArrayList<>();
    
    public void setLinks(List<Link> links) {
    	if(links != null) {
    		this.links = links;
    	}
    }
    
    private Long timeEntry;
    
    private Long timeStart;
    
    private String price;
    
    @JsonProperty("more_infos")
    @Lob
    private String moreInfos;
    
    @ElementCollection
    private List<String> type = new ArrayList<>();
    
    public void setType(List<String> type) {
    	if(type != null) {
    		this.type = type;
    	}
    }
    
    public void init() {
    	setFullEntryDate(getFullDate(timeEntry,date));
    	setFullStartDate(getFullDate(timeStart,date));
    }
    
    public String getLocationTitle() {
    	return getLocation() != null ? getLocation().getTitle() : StringUtils.EMPTY;
    }
    
    public String getStartTimeString() {
    	if (getFullStartDate() != null) {
    		return DateTimeFormatter.ofPattern("H:mm").format(getFullStartDate());
    	} else {
    		return StringUtils.EMPTY;
    	}
    }
    
    private boolean cancelled;
    
    private boolean favorite;
    
}
