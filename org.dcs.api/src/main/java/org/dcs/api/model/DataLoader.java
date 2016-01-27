package org.dcs.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-26T14:43:09.749+01:00")
public class DataLoader   {
  
  private String dataSourceId = null;

  
  /**
   * unique identifier for uploaded data file.
   **/
  
  @ApiModelProperty(value = "unique identifier for uploaded data file.")
  @JsonProperty("data_source_id")
  public String getDataSourceId() {
    return dataSourceId;
  }
  public void setDataSourceId(String dataSourceId) {
    this.dataSourceId = dataSourceId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataLoader dataLoader = (DataLoader) o;
    return Objects.equals(dataSourceId, dataLoader.dataSourceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataSourceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataLoader {\n");
    
    sb.append("    dataSourceId: ").append(toIndentedString(dataSourceId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

