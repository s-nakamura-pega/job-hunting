package sn.tools.demo.entity;

import sn.tools.db.annotation.DBColumn;
import sn.tools.db.annotation.DBTable;

@DBTable("customers")
public class Customers {


	@DBColumn("id")
	public Integer id;
	@DBColumn("name")
	public String name;
	@DBColumn("address")
	public String address;
	@DBColumn("phone")
	public String phone;
	@DBColumn("created_at")
	public String createdAt;

}
