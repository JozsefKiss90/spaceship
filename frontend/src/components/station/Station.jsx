import StationBar from './stationbar/StationBar';
import Messages from "./messages/Messages";
import "./Station.css";
import { StationContext } from "./MessageContext";
import { StorageStationContext } from "./StorageContext";
import { HangarStationContext } from "./HangarContext";
import { useOutletContext } from 'react-router-dom';

const Station = () => {
    const context = useOutletContext()
    const user = context.user;
    const stationId = context.stationId;

    if (user === null || stationId === null) {
        return <div>Loading...</div>
    }

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