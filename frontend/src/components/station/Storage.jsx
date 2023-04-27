import "./Storage.css";
import {useEffect, useState} from "react";
import {useStorageContext} from "../StorageContext";

const Storage = () => {
    const update = useStorageContext();
    console.log(update);
    const [storage, setStorage] = useState();

    useEffect(() => {
        fetch('http://localhost:8080/base/storage')
            .then((res) => {
                return res.json();
            })
            .then((data) => {
                setStorage(data);
            })
            .catch((err) => {
                console.error(err);
            });
    }, [update]);

    return (
        <>{!storage?"Loading...":(<div className="storage">
            <div className="menu">
                <div>{"STORAGE | " + storage.level + " | " + (storage.capacity - storage.freeSpace) + " / " + storage.capacity}</div>
                <div className="button">Upgrade</div>
            </div>
            {Object.keys(storage.resources).length === 0 ? (<div>No resources yet</div>) : (
                Object.keys(storage.resources).map((key) => {
                    return <p>{key.toLowerCase()}: {storage.resources[key]}</p>;
                })
            )}
        </div>)}</>

    );
}

export default Storage;