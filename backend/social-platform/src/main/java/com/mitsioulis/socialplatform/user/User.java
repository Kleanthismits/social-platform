package com.mitsioulis.socialplatform.user;

import java.beans.Transient;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.group.GroupSequenceProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonView;
import com.mitsioulis.socialplatform.shared.constants.Constants;
import com.mitsioulis.socialplatform.shared.validationGroups.FirstValidation;
import com.mitsioulis.socialplatform.shared.validationGroups.SecondValidation;
import com.mitsioulis.socialplatform.shared.validationGroups.ThirdValidation;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@GroupSequenceProvider(UserGroupSequenceProvider.class)
public class User implements UserDetails {

	private static final long serialVersionUID = 1499502615415736719L;

	@Id
	@GeneratedValue
	@JsonView(Views.Base.class)
	private Integer id;
	@NotNull(message = "{socialplatform.constraints.username.NotNull.message}", groups = FirstValidation.class)
	@Size(min = 4, max = 255, groups = SecondValidation.class)
	@UniqueUserName(groups = SecondValidation.class)
	@JsonView(Views.Base.class)
	private String  username;
	@NotNull(groups = FirstValidation.class)
	@Size(min = 4, max = 255, groups = SecondValidation.class)
	@JsonView(Views.Base.class)
	private String  displayName;
	@NotNull(groups = FirstValidation.class)
	@Size(min = 8, max = 255, groups = SecondValidation.class)
	@Pattern(regexp = Constants.Regex.PASSWORD_REGEX, groups = ThirdValidation.class)
	private String  password;

	@JsonView(Views.Base.class)
	private String image;

	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Role_USER");
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}

}
