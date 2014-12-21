/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.domain.service.logger;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoggerDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty
    private String name;
    @NotEmpty
    @ExistInCodeList(codeListId = "CL_LOGGER_LEVEL")
    private String level;
}
