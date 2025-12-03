package dev.onebite.api.persentation.dto.response;

public record ApiResponse<T>(boolean success, T data) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data);
    }

    public static <T> ApiResponse<T> fail() {
        return new ApiResponse<>(false, null);
    }

}
