package qna.question;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static qna.common.QnaConst.MIN_TEXT_LENGTH;

@Embeddable
public class Title {
    private static final int MAX_LENGTH_TITLE = 100;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    public Title(final String title) {
        this.title = title;
        validateTitle();
    }

    protected Title() {
    }

    private void validateTitle() {
        if (Objects.isNull(title) || isInvalidTitleLength()) {
            throw new IllegalArgumentException("질문의 제목 길이는 최소 1이상 100이하여야 합니다.");
        }
    }

    private boolean isInvalidTitleLength() {
        return title.length() < MIN_TEXT_LENGTH || title.length() > MAX_LENGTH_TITLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title1 = (Title) o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}