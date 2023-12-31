import styled from "styled-components";

import defaultUserImg from "../../../img/default_user.png";

const Wrapper = styled.div`
  position:relative;
  top:8px;
  margin-right:16px;
`;

const Img = styled.img`
    width: 26px;
    height: 26px;
    border-radius:4px;
`;

const OnlineStatus = styled.i`
    position:relative;
    top:-10px;
    left:16px;
    display:block;
    width:16px;
    height:16px;
    border-radius:8px;
    background-color:green;
    border:3px solid #121016;
`;

function UserIcon() {
    return (
        <Wrapper>
            <Img src={defaultUserImg}/>
            <OnlineStatus></OnlineStatus>
        </Wrapper>

    )
}

export default UserIcon;