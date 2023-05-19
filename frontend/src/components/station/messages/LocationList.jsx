import { useEffect, useState } from "react";
import { useOutletContext } from "react-router-dom";
import "./LocationList.css";
import Location from "./Location";

export default function LocationList() {
    const {user, jwt} = useOutletContext();
    const [locations, setLocations] = useState(null);

    useEffect(() => {
        fetch(`/api/location?user=${user.userId}`, {
            headers: {
            Authorization: `Bearer ${jwt}`
          }})
          .then(res => res.json())
          .then(data => setLocations(data));
    },[user, jwt])
    
    if(locations === null) {
        return <div>Loading...</div>
    }

    return <div className="location-list">
        <div>Available locations:</div>
        {locations.map(location => <Location key={location.id} location={location}/>)}
    </div>
}
