package ca.sheridancollege.doricha.beans;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

	private Long userId;
	@NonNull
	private String email;
	@NonNull
	private String encryptedPassword;
	@NonNull
	private boolean enabled;
	
}
