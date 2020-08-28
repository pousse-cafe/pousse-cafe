package poussecafe.storage;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static poussecafe.util.Equality.referenceEquals;

public class Page {

    public static final int DEFAULT_NUMBER = 0;

    public static final int DEFAULT_SIZE = 10;

    public static final long DEFAULT_OFFSET = offset(DEFAULT_NUMBER, DEFAULT_SIZE);

    private static final long offset(int number, int size) {
        return (long) number * size;
    }

    private int number = DEFAULT_NUMBER;

    public int number() {
        return number;
    }

    private int size = DEFAULT_SIZE;

    public int size() {
        return size;
    }

    public long offset() {
        return offset(number, size);
    }

    public static class Builder {

        private Page page = new Page();

        public Page build() {
            if(offset > 0) {
                if(page.size <= 0) {
                    throw new IllegalStateException("Size must be >0 when offset is provided");
                }
                page.number = (int) (offset / page.size);
            } else {
                if(page.size < 0 || page.number < 0) {
                    throw new IllegalStateException("Size and number must be >=0");
                }
            }
            return page;
        }

        public Builder number(int number) {
            page.number = number;
            return this;
        }

        public Builder size(int size) {
            page.size = size;
            return this;
        }

        public Builder offset(long offset) {
            this.offset = offset;
            return this;
        }

        private long offset = -1;
    }

    private Page() {

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(number)
                .append(size)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(number)
                .append(size)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(number, other.number)
                .append(size, other.size)
                .build());
    }
}
