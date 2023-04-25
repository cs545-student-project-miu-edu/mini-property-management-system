import { useRef, useState } from "react";
import { authenticateUser } from "../../services/api/authentication";
import { authenticationSuccess } from "../../feature/Authentication/authenticationSlice";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo } from "../../services/api/loggedinUser";
import { userinfoSuccess } from "../../feature/Authentication/loggedinUserSlice";

export const AuthenticationWidget = () => {
  const authenticationForm = useRef();
  const [authenticationStoreValueChanged, setAuthenticationStoreValueChanged] = useState(false);
  const [loggedinUserStoreValueChanged, setLoggedinUserStoreValueChanged] = useState(false);
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const authenticate = async (event) => {
    event.preventDefault();
    console.log("Before Store Token:" + store?.authentication?.value?.token);
    let authResult = await authenticateUser(
      authenticationForm.current["username"].value,
      authenticationForm.current["password"].value
    );
    console.log("Returned Token from API" + authResult.token);
    await dispatch(authenticationSuccess(authResult));
    let userInfo = await getUserInfo();
    console.log("Returned UserInfo from API" + userInfo.name);
    dispatch(userinfoSuccess(userInfo));
    setAuthenticationStoreValueChanged(true);
    setLoggedinUserStoreValueChanged(true);
  };
  return (
    <>
      {store?.authentication?.value?.token ? (
         <div className="nav-link active">
         Welcome {store?.loggedinUser?.value?.name}
       </div>
      ) : (
        <form ref={authenticationForm} onSubmit={authenticate}>
          <input name="username"></input>
          <br />
          <input name="password"></input>
          <br />
          <button>Login</button>
        </form>
      )}
    </>
  );
};
