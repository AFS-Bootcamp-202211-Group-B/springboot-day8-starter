package com.rest.springbootemployee;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {
	@Autowired
	MockMvc client;

	@Autowired
	EmployeeRepository employeeRepository;

	@BeforeEach
	void cleanRepository(){
		employeeRepository.clearAll();
	}

	@Test
	void should_get_all_employees_when_perform_get_given_employees() throws Exception{
		//given
		employeeRepository.create(new Employee(10,"test",22,"Female",10000));
		employeeRepository.create(new Employee(10,"test",22,"Female",10001));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees"))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].name",containsInAnyOrder("test","test")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(22))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value(10001));

	}
	@Test
	void should_get_employee_by_id_when_perfrm_get_by_id_given_employees() throws Exception{
		//given
		Employee test1=employeeRepository.create(new Employee(10,"test",22,"Female",10000));
		Employee test2=employeeRepository.create(new Employee(10,"test",22,"Female",10001));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees/{id}",test1.getId()))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
	}

	@Test
	void should_get_male_employee_when_perform_get_by_gender_given_employees() throws Exception{
		//given
		Employee test1=employeeRepository.create(new Employee(10,"test",22,"Female",10000));
		Employee test2=employeeRepository.create(new Employee(10,"test",22,"Male",10001));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}","Male"))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].gender").value("Male"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].salary",containsInAnyOrder(10001)));
	}
	@Test
	void should_get_employee_by_page_when_perform_get_by_page_given_employees() throws Exception{
		//given
		Employee test1=employeeRepository.create(new Employee(10,"test",22,"Female",10000));
		Employee test2=employeeRepository.create(new Employee(10,"test",22,"Male",10001));
		Employee test3=employeeRepository.create(new Employee(10,"test",22,"Male",10001));
		//when
		client.perform(MockMvcRequestBuilders.get("/employees?page=1&pageSize=2"))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(22))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value("Male"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value(10001));
	}

	//test post new employee
	@Test
	void should_create_new_employee_when_perform_create_given_employees() throws Exception{
		//given
		String newUserJsonString="{\"name\":\"Jim\",\"age\":20,\"gender\":\"Male\",\"salary\":55000}";
		//when
		MvcResult result = client.perform(MockMvcRequestBuilders.post("/employees").content(newUserJsonString).contentType(MediaType.APPLICATION_JSON))
				//Then
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jim"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(55000))
				.andReturn();
//
		int newUserId = JsonPath.read(result.getResponse().getContentAsString(),"$.id");
		client.perform(MockMvcRequestBuilders.get("/employees/{id}",newUserId))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jim"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(55000));

	}

	//test put employee
	@Test
	void should_update_employee_when_perform_put_given_employees() throws Exception{
		//given
		Employee test1=employeeRepository.create(new Employee(10,"test",22,"Female",10000));
		String newUserJsonString="{\"name\":\"Jim\",\"age\":20,\"gender\":\"Male\",\"salary\":55000}";
		//when
		client.perform(MockMvcRequestBuilders.put("/employees/{id}",test1.getId())
				.content(newUserJsonString).contentType(MediaType.APPLICATION_JSON))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(55000));
		client.perform(MockMvcRequestBuilders.get("/employees/{id}",test1.getId()))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
				.andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(55000));

	}

	//test delete employee
	@Test
	void should_delete_employee_by_id_when_perform_delete_given_employees() throws Exception{
		//given
		Employee test1=employeeRepository.create(new Employee(10,"test",22,"Female",10000));
		//when
		client.perform(MockMvcRequestBuilders.delete("/employees/{id}",test1.getId()))
				//Then
				.andExpect(MockMvcResultMatchers.status().isNoContent());


		client.perform(MockMvcRequestBuilders.get("/employees"))
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(0)));

	}





}