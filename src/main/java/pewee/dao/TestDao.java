package pewee.dao;

import java.util.List;

import pewee.entity.TestEntity;

public interface TestDao {
	
	public int insert(TestEntity testEntity);
	
	public int batchinsert(List<TestEntity> testEntitylist);
	
	public TestEntity getById(int i);
	
	public int updateBatch(List<TestEntity> testEntitylist) ;
	
}
