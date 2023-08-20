
import NavbarSearchForm from "./NavbarSearchForm";
import UserIcon from "./UserIcon";
import NavbarFrame from "./NavbarFrame";

function Navbar() {
    return (
        <NavbarFrame>
            <div></div>
            <NavbarSearchForm/>
            <UserIcon/>
        </NavbarFrame>
    )
}

export default Navbar;