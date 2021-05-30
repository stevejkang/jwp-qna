package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("유저 저장")
    void save() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");

        // when
        users.save(user1);
        User actual = users.findByUserId("USER1").orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isSameAs(user1);
    }

    @Test
    @DisplayName("이름 변경 시 updatedAt 컬럼 업데이트 확인")
    void modifyName() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");

        // when then
        User actual = users.save(user1);
        actual.modifyName("LJS");
        users.flush();
    }

    @Test
    @DisplayName("변경감지 실습 - 원래의 이름으로 다시 세팅 시, 업데이트 쿼리를 실행하지 않음")
    void study_dirtyCheck() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");

        // when then
        User actual = users.save(user1);
        actual.modifyName("LJS");
        actual.modifyName("LDS");
    }

    @Test
    @DisplayName("이름 기반으로 유저 찾기")
    void findByName() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");

        // when
        users.save(user1);
        User actual = users.findByName("LDS").orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isSameAs(user1);
    }

    @Test
    @DisplayName("유저명은 20자 이하여야 함")
    void userNameLength() {
        // given
        User user = new User("USER1", "123456", "123456789012345678901", "lds@test.com");

        // when then
        assertThatThrownBy(
                () -> users.save(user)
        ).isInstanceOf(DataIntegrityViolationException.class);

    }
}