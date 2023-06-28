import StationBar from "./stationbar/StationBar";
import "./Station.css";
import "./messages/Messages.css";
import { StorageStationContext } from "./StorageContext";
import { HangarStationContext } from "./HangarContext";
import { Outlet, useOutletContext } from "react-router-dom";

const Station = () => {
  const context = useOutletContext();
  const user = context.user;
  const stationId = context.stationId;

  if (user === null || stationId === null) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <div className="station">
          <StorageStationContext>
            <HangarStationContext>
              <StationBar></StationBar>
              <div className="message-log">
                <div className="messages">
                  <Outlet context={{ ...context }} />
                </div>
              </div>
            </HangarStationContext>
          </StorageStationContext>
      </div>
    </>
  );
};

export default Station;
