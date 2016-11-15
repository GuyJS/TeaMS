package message;

import java.io.Serializable;

public interface Message extends Serializable {

    String getMessage();

    String getName();
}
