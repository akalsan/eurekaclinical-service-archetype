#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.resource;

/*-
 * ${symbol_pound}%L
 * ${artifactId}
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ${symbol_pound}L%
 */

import com.google.inject.persist.Transactional;
import ${groupId}.common.comm.User;
import ${groupId}.common.resource.AbstractUserResource;
import ${package}.entity.RoleEntity;
import ${package}.entity.UserEntity;
import ${groupId}.standardapis.dao.RoleDao;
import ${groupId}.standardapis.dao.UserDao;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akalsan on 10/5/16.
 */
@Path("/protected/users")
@Transactional
public class UserResource extends AbstractUserResource<User, UserEntity, RoleEntity> {
	private final RoleDao<RoleEntity> roleDao;

	@Inject
	public UserResource(UserDao<UserEntity> inUserDao, RoleDao<RoleEntity> inRoleDao) {
		super(inUserDao);
		this.roleDao = inRoleDao;
	}

	@Override
	protected User toComm(UserEntity userEntity, HttpServletRequest req) {
		User user = new User();
		user.setId(userEntity.getId());
		user.setUsername(userEntity.getUsername());
		List<Long> roles = new ArrayList<>();
		for (RoleEntity roleEntity : userEntity.getRoles()) {
			roles.add(roleEntity.getId());
		}
		user.setRoles(roles);
		return user;
	}

	@Override
	protected UserEntity toEntity(User user) {
		List<RoleEntity> roleEntities = this.roleDao.getAll();
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setUsername(user.getUsername());
		List<RoleEntity> userRoleEntities = new ArrayList<>();
		for (Long roleId : user.getRoles()) {
			for (RoleEntity roleEntity : roleEntities) {
				if (roleEntity.getId().equals(roleId)) {
					userRoleEntities.add(roleEntity);
				}
			}
		}
		userEntity.setRoles(userRoleEntities);
		return userEntity;
	}
}
