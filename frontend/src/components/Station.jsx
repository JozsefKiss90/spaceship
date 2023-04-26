import StationBar from './station/StationBar';
import Messages from "./station/Messages";
import "./Station.css";
import {StationContext} from "./MessageContext";

const Station = () => {

    return (<div className="station">
        <StationContext>
            <StationBar></StationBar>
            <Messages></Messages>
        </StationContext>
    </div>);
}

export default Station;