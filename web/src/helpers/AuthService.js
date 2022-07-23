import jwtService from "@/helpers/JwtService";

export const isAuthenticated = () => {
    return !!jwtService.getToken();
}

export default {isAuthenticated};
