package io.swagger.model;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-01-14T16:15:36.482+01:00")
public class Module  {
  
  private String productId = null;
  private String description = null;
  private String displayName = null;

  
  /**
   * Unique identifier representing a specific module.
   **/
  @ApiModelProperty(value = "Unique identifier representing a specific module.")
  @JsonProperty("product_id")
  public String getProductId() {
    return productId;
  }
  public void setProductId(String productId) {
    this.productId = productId;
  }

  
  /**
   * Description of module.
   **/
  @ApiModelProperty(value = "Description of module.")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   * Display name of module.
   **/
  @ApiModelProperty(value = "Display name of module.")
  @JsonProperty("display_name")
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Module {\n");
    
    sb.append("  productId: ").append(productId).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
