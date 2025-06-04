import { TestBed } from '@angular/core/testing';

import { ProjectUserService } from './project-user.service';
import {TaskService} from "./task.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ProjectUserService', () => {
  let service: ProjectUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProjectUserService]
    });
    service = TestBed.inject(ProjectUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
