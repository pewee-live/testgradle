package pewee.entity;

public class TestEntity {
	
	private int id;
	
	private String name;
	
	private String other;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return "TestEntity [id=" + id + ", name=" + name + ", other=" + other + "]";
	}
	
	
	
}
