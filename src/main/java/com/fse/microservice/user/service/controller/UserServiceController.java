package com.fse.microservice.user.service.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fse.microservice.user.service.entity.UserEntity;
import com.fse.microservice.user.service.repository.UserServiceRepository;

/*@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)*/
@RestController
@RequestMapping(value="/users")
public class UserServiceController {

	@Autowired
	private UserServiceRepository userServiceRepository;
	
	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public ResponseEntity<String> addUser(@RequestBody UserEntity entity)
	{
		userServiceRepository.save(entity);
		return new ResponseEntity<String>("User Added Successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getuser", method = RequestMethod.GET)
	public ResponseEntity<UserEntity> getUser(@RequestParam("id") String userid)
	{
		UserEntity userEntity = new UserEntity();
		if(!StringUtils.isEmpty(userid))
		{
			int id= Integer.parseInt(userid);
			userEntity = (UserEntity) userServiceRepository.findByuserid(id);
			return new ResponseEntity<UserEntity>(userEntity,HttpStatus.OK);
		}
		return new ResponseEntity<UserEntity>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/getalluser", method = RequestMethod.GET)
	public ResponseEntity<List<UserEntity>> getAllUser()
	{
		List<UserEntity> userEntity = new ArrayList<UserEntity>();
		userEntity = (List<UserEntity>) userServiceRepository.findAll();
		if(userEntity != null)
		{
			return new ResponseEntity<List<UserEntity>>(userEntity,HttpStatus.OK);
		}
		return new ResponseEntity<List<UserEntity>>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/getallsorteduser", method = RequestMethod.GET)
	public ResponseEntity<List<UserEntity>> getAllSortedUser(@RequestParam("sortname") String sortname)
	{
		Sort sortOrder = Sort.by(sortname);
		List<UserEntity> userEntity = new ArrayList<UserEntity>();
		userEntity = (List<UserEntity>) userServiceRepository.findAll(sortOrder);
		if(userEntity != null)
		{
			return new ResponseEntity<List<UserEntity>>(userEntity,HttpStatus.OK);
		}
		return new ResponseEntity<List<UserEntity>>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/updateuser", method = RequestMethod.POST)
	public ResponseEntity<String> updateUser(@RequestBody UserEntity entity)
	{
		
		if( userServiceRepository.findByuserid(entity.getUserid()) != null)
		{
			userServiceRepository.save(entity);
			return new ResponseEntity<String>("User Detail Updated Successfully",HttpStatus.OK);
		}
		return new ResponseEntity<String>("No User Found",HttpStatus.BAD_REQUEST);
	}
	
	
	@RequestMapping(value = "/deleteuser", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteUser(@RequestParam("id") String id)
	{
		 if(!StringUtils.isEmpty(id))
		 {
			 int userid = Integer.parseInt(id);
			 userServiceRepository.deleteById(userid); 
			 return new	 ResponseEntity<String>("User Detail Deleted",HttpStatus.OK); }
		 
		
		return new ResponseEntity<String>("User Detail Not Found",HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/searchuser", method = RequestMethod.GET)
	public ResponseEntity<List<UserEntity> > searchUser(@RequestParam("searchby") String searchby,
			@RequestParam("value") String value)
	{
		List<UserEntity> userEntity = new ArrayList<UserEntity>();
		if(searchby != null && !searchby.isEmpty() && value != null && !value.isEmpty())
		{
			if(searchby.equalsIgnoreCase("firstname"))
			{
				userEntity = (List<UserEntity> ) userServiceRepository.findByfirstname(value);
			}
			else if(searchby.equalsIgnoreCase("lastname"))
			{
				userEntity = (List<UserEntity> ) userServiceRepository.findBylastname(value);
			}
			else if(searchby.equalsIgnoreCase("employeeid"))
			{
				userEntity = (List<UserEntity> ) userServiceRepository.findByemployeeid(value);
			}
			return new ResponseEntity<List<UserEntity> >(userEntity,HttpStatus.OK);
			
		}
		return new ResponseEntity<List<UserEntity>>(HttpStatus.BAD_REQUEST);
	}
}
