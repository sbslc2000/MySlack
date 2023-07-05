import {useEffect, useState} from "react";
import axios from "axios";


function WorkspaceSelector() {

    const [workspaces, setWorkspaces] = useState([]);

    useEffect(() => {
        axios.get("/api/users/me/workspaces").then((response) => {
            console.log(response.data);
            if(response.data.isSuccess) {
                setWorkspaces(response.data.result);
            }
        });
    },[]);

    let dropdownMenu;

    if(workspaces.length === 0) {
        dropdownMenu = <li><a className="dropdown-item" href="#">현재 참여중인 워크스페이스가 없어요.</a></li>;
    } else {
        dropdownMenu = workspaces.map((workspace) => {
            return (
                <li><a className="dropdown-item" href={"/app/workspace/"+workspace.id}>{workspace.name}</a></li>
            )
        });
    }

    return (
        <div className="dropdown">
            <a className="btn btn-primary dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
                워크스페이스 선택하기
            </a>

            <ul className="dropdown-menu">
                {dropdownMenu}
                <li>
                    <hr className="dropdown-divider"/>
                </li>
                <li><a className="dropdown-item" href="/workspace/create">새 워크스페이스 생성</a></li>
            </ul>
        </div>
    );
}

export default WorkspaceSelector;