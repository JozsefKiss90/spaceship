import "./Hangar.css";

const Hangar = (props) => {
    const hangar = props.hangar;

    return (<div className="hangar">
        <div className="menu">
            <div>{"HANGAR | " + hangar.level + " | " + hangar.freeDocks + " / " + hangar.capacity}</div>
            <div className="button">Upgrade</div>
        </div>
        {Object.keys(hangar.ships).length === 0 ? (<div>No ships yet</div>) : (
            hangar.ships.map((ship) => {
                return <p>{ship.name} - {ship.type}</p>;
            })
        )}
    </div>);

}

export default Hangar;