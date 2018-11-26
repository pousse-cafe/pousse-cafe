package poussecafe.spring.kafka;

import java.util.Objects;
import org.springframework.kafka.core.KafkaTemplate;
import poussecafe.jackson.JacksonMessageAdapter;
import poussecafe.messaging.MessageSender;

public class KafkaMessageSender extends MessageSender {

    public static class Builder {

        private KafkaMessageSender messageSender = new KafkaMessageSender();

        public Builder kafkaTemplate(KafkaTemplate<String, String> template) {
            messageSender.template = template;
            return this;
        }

        public Builder topic(String topic) {
            messageSender.topic = topic;
            return this;
        }

        public KafkaMessageSender build() {
            Objects.requireNonNull(messageSender.template);
            Objects.requireNonNull(messageSender.topic);
            return messageSender;
        }
    }

    private KafkaMessageSender() {
        super(new JacksonMessageAdapter());
    }

    private KafkaTemplate<String, String> template;

    private String topic;

    @Override
    protected void sendMarshalledMessage(Object marshalledMessage) {
        template.send(topic, (String) marshalledMessage);
    }

}
