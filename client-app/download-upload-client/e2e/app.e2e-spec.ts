import { DownloadUploadClientPage } from './app.po';

describe('download-upload-client App', function() {
  let page: DownloadUploadClientPage;

  beforeEach(() => {
    page = new DownloadUploadClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
