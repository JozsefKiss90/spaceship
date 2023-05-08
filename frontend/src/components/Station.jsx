import StationBar from './station/StationBar';
import Messages from "./station/Messages";
import "./Station.css";
import {StationContext} from "./MessageContext";
import {StorageStationContext} from "./StorageContext";
import {HangarStationContext} from "./HangarContext";

const Station = () => {

    return (<div className="station">
        <StationContext>
            <StorageStationContext>
                <HangarStationContext>
                    <StationBar></StationBar>
                    <Messages></Messages>
                </HangarStationContext>
            </StorageStationContext>
        </StationContext>
    </div>);
}

export default Station;