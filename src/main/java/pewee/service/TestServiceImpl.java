package pewee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pewee.dao.TestDao;
import pewee.entity.TestEntity;
@Service
public class TestServiceImpl implements TestService {
	
	@Autowired
	TestDao td;
	
	@Override
	@Transactional
	public void insert(TestEntity testEntity) {
		// TODO Auto-generated method stub
		td.insert(testEntity);
	}

	@Override
	public TestEntity getById(int i) {
		// TODO Auto-generated method stub
		return td.getById(i);
	}

	@Override
	public int batchinsert(List<TestEntity> testEntitylist) {
		// TODO Auto-generated method stub
		return td.batchinsert(testEntitylist);
	}

	@Override
	public int updateBatch(List<TestEntity> testEntitylist) {
		// TODO Auto-generated method stub
		return td.updateBatch(testEntitylist);
	}

}
