import "./Messages.css";
import {useMessageContext} from "../MessageContext";
const Messages = () => {
    const message = useMessageContext();
    return <div className="message-log">
        <div className="messages">{message}</div>
    </div>;
}

export default Messages;