
import defaultUserImg from "../../../img/default_user.png";

import styled from "styled-components";
import NavbarSearchForm from "./NavbarSearchForm";
import UserIcon from "./UserIcon";
import NavbarFrame from "./NavbarFrame";


const Wrapper = styled.nav`
    height:44px;
    background-color: #121016;
`;




function Navbar() {
    return (
        <NavbarFrame className="navbar justify-content-center">
            <div style={{width:588,height:26}}>
            </div>
            <NavbarSearchForm/>
            <div style={{width:588,height:26,paddingRight:16}} className="d-flex justify-content-end">
                <UserIcon/>
            </div>
        </NavbarFrame>
    )
}

export default Navbar;