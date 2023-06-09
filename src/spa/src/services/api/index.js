import axiosUnsecuredInstance from "axios";
import axiosSecuredInstance from "axios";
import { loggedinUserHasRole, loggedinUserOwnerApprovalPending } from "../../feature/Authentication/loggedinUserSlice";

let storeState;
let errorMessagesContext;

export const injectStoreState = (_storeState) => {
  storeState = _storeState;
};

export const injectErrorMessagesContext = (_errorMessagesContext) => {
  errorMessagesContext = _errorMessagesContext;
};

export const apiUnsecured = axiosUnsecuredInstance.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor for API calls
axiosSecuredInstance.interceptors.request.use(
  async (config) => {
    const token = storeState?.authentication?.value?.token;
    config.headers = {
      Authorization: `Bearer ${token}`,
      Accept: "application/json",
      "Content-Type": "application/json",
    };
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

export const apiSecured = () => {
  const token = storeState?.authentication?.value?.token;
  let headers = {
    Accept: "application/json",
    "Content-Type": "application/json",
  };
  if (token) {
    headers = { ...headers, Authorization: `Bearer ${token}` };
  }
  let instance = axiosSecuredInstance.create({
    baseURL: "http://localhost:8080",
    headers: headers,
  });
  return instance;
};

export const hasRole = (roleName) => {
  return loggedinUserHasRole(storeState, roleName);
}

export const ownerApprovalPending = (roleName) => {
  return loggedinUserOwnerApprovalPending(storeState);
}

export const getStoreState = () => {
  return storeState;
}

export const getErrorMessagesContext = () => {
  return errorMessagesContext;
}