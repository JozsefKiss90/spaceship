import "./Storage.css";

const Storage = (props) => {
    const storage = props.storage;


    return (
        <div className="storage">
                <div className="menu">
                    <div>{"STORAGE | " + storage.level + " | " + (storage.capacity - storage.freeSpace) + " / " + storage.capacity}</div>
                    <div className="button">Upgrade</div>
                </div>
                {Object.keys(storage.resources).length === 0 ? (<div>No resources yet</div>) : (
                    Object.keys(storage.resources).map((key) => {
                        return <p>{key.toLowerCase()}: {storage.resources[key]}</p>;
                    })
                )}

        </div>
    );
}

export default Storage;