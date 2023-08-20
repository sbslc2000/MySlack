import styled from "styled-components";


const Input = styled.input`
    display: block;
    background-color: #38373C;
    width:732px;
    height:26px;
    border: 1px solid #535155;
    border-radius:4px;
    color:#D1D2D3;
    font-size:13px;
    
    &:focus {
    background-color: #38373C;
    color:white;
    }
  
  &::placeholder {
    color:#D1D2D3;
    font-size:13px;
  }
`;
function NavbarSearchForm() {
    return (
        <Input className="form-control me-2" type="search" placeholder="워크스페이스에서 검색" aria-label="Search" />
    )
};

export default NavbarSearchForm;