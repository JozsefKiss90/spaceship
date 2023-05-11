import StationBar from './station/StationBar';
import Messages from "./station/Messages";
import "./Station.css";
import { StationContext } from "./MessageContext";
import { StorageStationContext } from "./StorageContext";
import { HangarStationContext } from "./HangarContext";
import { useOutletContext } from 'react-router-dom';

const Station = () => {
    const [, , user, , stationId] = useOutletContext()

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