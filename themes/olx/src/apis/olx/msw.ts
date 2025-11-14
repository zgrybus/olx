import { createOpenApiHttp } from "openapi-msw";
import type { paths } from "./generated/client";

export const olxHttpMsw = createOpenApiHttp<paths>();