import styled from "styled-components";

import defaultUserImg from "../../../img/default_user.png";

const Wrapper = styled.img`
    display:inline-block
    width: 20px;
    height: 20px;
    border-radius:4px;
`;

const OnlineStatus = styled.i`
    position:relative;
    top:10px;
    left:-8px;
    display:inline-block;
    width:12px;
    height:12px;
    border-radius:6px;
    background-color:${props => props.online ? "green" : "gray"};
    border:2px solid #121016;
`;

function SmallUserIcon(props) {

    const user = props.user;
    return (
        <div>
            <Wrapper src={user.profileImage}>

            </Wrapper>
            <OnlineStatus online={user.active}></OnlineStatus>
        </div>

    )
}

export default SmallUserIcon;