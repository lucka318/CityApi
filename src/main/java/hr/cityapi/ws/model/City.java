package hr.cityapi.ws.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class City {

	private Long id;
	private String name;
	private String description;
	private Long population;
	private Date creationDate = new Date();
	private Long favouriteCount = new Long(0);
	
	@JsonIgnore
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPopulation() {
		return population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}

	
	public Date getCreationDate() {
		return creationDate;
	}

	@JsonIgnore
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	
	public Long getFavouriteCount() {
		return favouriteCount;
	}

	@JsonIgnore
	public void setFavouriteCount(Long favoriteCount) {
		this.favouriteCount = favoriteCount;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", description="
				+ description + ", population=" + population
				+ ", creationDate=" + creationDate + ", favouriteCount="
				+ favouriteCount + "]";
	}

}
