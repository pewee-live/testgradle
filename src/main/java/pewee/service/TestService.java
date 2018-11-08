package pewee.service;

import java.util.List;

import pewee.entity.TestEntity;

public interface TestService {
	
	public void insert(TestEntity testEntity);
	
	public TestEntity getById(int i);
	
	public int batchinsert(List<TestEntity> testEntitylist);
	
	public int updateBatch(List<TestEntity> testEntitylist) ;
	
}
