package pewee.controller;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pewee.entity.TestEntity;
import pewee.service.TestService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@RestController
public class TestController{
	
	@Autowired
	TestService ts;
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	
	private static Scheduler scheduler = Schedulers.newParallel("parralPool", 100);
	
	@RequestMapping("/hello")
	public String hello(@RequestParam(value="name",defaultValue="pewee") String name) {
		return name + "_hello";
	}
	
	@RequestMapping("/insert")
	public String insert(@RequestBody TestEntity testEntity) {
		
		logger.info("insert:\t" + testEntity);
		
		try {
			ts.insert(testEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "fail";
		}
		return "succ";
		
	}
	
	@RequestMapping("/insertBatch")
	public String insertBatch(@RequestBody List<TestEntity> testEntityLists) {
		
		logger.info("insertbatch:\t" + testEntityLists);
		
		try {
			ts.batchinsert(testEntityLists);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "fail";
		}
		return "succ";
		
	}
	
	@RequestMapping("/updateBatch")
	public String updateBatch(@RequestBody List<TestEntity> testEntityLists) {
		
		logger.info("updateBatch:\t" + testEntityLists);
		
		try {
			ts.updateBatch(testEntityLists);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "fail";
		}
		return "succ";
		
	}
	
	
}
