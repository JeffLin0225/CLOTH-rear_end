package com.cloth.service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloth.Repository.AdminRepository;
import com.cloth.Repository.CustomerserviceRepository;
import com.cloth.Repository.UsersRepository;
import com.cloth.model.Admin;
import com.cloth.model.Customerservice;
import com.cloth.model.Users;

@Service
@Transactional
public class CustomerserviceService {
	@Autowired
	private CustomerserviceRepository customerserviceRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private UsersRepository usersRepository;
	
	public Customerservice create(String chats,Integer adminid,Integer usersid,List<MultipartFile> files) {
		try {
			Customerservice insert = new Customerservice();
			insert.setChats(chats);
			if(adminid!=null) {
			Optional<Admin>optional	= adminRepository.findById(adminid);
				Admin adm =optional.get();
				insert.setAdmin(adm);
			}
			if(usersid!=null) {
				Optional<Users>optional	= usersRepository.findById(usersid);
				Users user =optional.get();
				insert.setUsers(user);			
			}			
			insert.setSend_at(new Date());
			insert.setIsUserRead(0);
			insert.setIsAdminRead(1);
			if (files != null) {
				for (int i = 0; i < files.size(); i++) {
					MultipartFile file = files.get(i);
					switch (i) {
						case 0:
							if (file == null) {
								file = null;
								break;
							}
							insert.setImg1(file.getBytes());
							break;
//						case 1:
//							if (file == null) {
//								file = null;
//								break;
//							}
//							insert.setImg2(file.getBytes());
//							break;
//						case 2:
//							if (file == null) {
//								file = null;
//								break;
//							}
//							insert.setImg3(file.getBytes());
//							break;
						default:
							break;
					}
				}

			}
			return customerserviceRepository.save(insert);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//==========================================================================================
	public Customerservice createbyUser(String chats,Integer usersid,List<MultipartFile> files) {
		try {
			Customerservice insert = new Customerservice();
			insert.setChats(chats);
			if(usersid!=null) {
				Optional<Users>optional	= usersRepository.findById(usersid);
				Users user =optional.get();
				insert.setUsers(user);			
			}			
			insert.setSend_at(new Date());
			insert.setIsUserRead(1);
			insert.setIsAdminRead(0);
			if (files != null) {
				for (int i = 0; i < files.size(); i++) {
					MultipartFile file = files.get(i);
					switch (i) {
						case 0:
							if (file == null) {
								file = null;
								break;
							}
							insert.setImg1(file.getBytes());
							break;

						default:
							break;
					}
				}

			}
			return customerserviceRepository.save(insert);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
