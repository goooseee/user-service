package com.example.mapper;

import com.example.DTO.UserDTOResponse;
import com.example.entity.User;

public class UserMapper {
	
	public UserDTOResponse userToDTO(User user) {
		if(user==null) {
			return null;
		}
		
		return new UserDTOResponse(user.getId(),
				user.getName(),
				user.getEmail(),
				user.getAge(),
				user.getCreatedAt());
	}
	
}
