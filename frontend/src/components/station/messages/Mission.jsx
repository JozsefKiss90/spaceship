
function Mission(props) {
    const mission = props.message.data;

    return (<>
    <div>
        <div>Mission status: {mission.status}</div>
        <div>Mission type: {mission.missionType}</div>
        <div>Location: {mission.location}</div>
        <div>Events: {mission.eventLog.split('\n').map((log)=><div>{log}</div>)}</div>
        <div>Mission end: {new Date(mission.approxEndTime).toLocaleString()}</div>
    </div>
    </>)
}

export default Mission