package in.stonecolddev.bocean;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it-test")
@Tag("it-test")
class BOceanApplicationTests {

	@Test
	void contextLoads() {
	}

}
