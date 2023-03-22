package com.techgalery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  private String id;
  private String name;
  private String type;
  private String version;
  private String event;
}