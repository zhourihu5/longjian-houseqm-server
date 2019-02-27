<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:o="urn:schemas-microsoft-com:office:office"
          xmlns:x="urn:schemas-microsoft-com:office:excel"
          xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:html="http://www.w3.org/TR/REC-html40">
    <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
        <Author>xbany</Author>
        <LastAuthor>xbany</LastAuthor>
        <Created>2019-02-27T06:18:37Z</Created>
        <Version>12.00</Version>
    </DocumentProperties>
    <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
        <WindowHeight>9432</WindowHeight>
        <WindowWidth>22968</WindowWidth>
        <WindowTopX>96</WindowTopX>
        <WindowTopY>24</WindowTopY>
        <ProtectStructure>False</ProtectStructure>
        <ProtectWindows>False</ProtectWindows>
    </ExcelWorkbook>
    <Styles>
        <Style ss:ID="Default" ss:Name="Normal">
            <Alignment ss:Vertical="Center"/>
            <Borders/>
            <Font ss:FontName="宋体" x:CharSet="134" ss:Size="11" ss:Color="#000000"/>
            <Interior/>
            <NumberFormat/>
            <Protection/>
        </Style>
        <Style ss:ID="s63">
            <Alignment ss:Horizontal="Center" ss:Vertical="Center" ss:WrapText="1"/>
            <Borders>
                <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"
                        ss:Color="#000000"/>
                <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"
                        ss:Color="#000000"/>
                <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"
                        ss:Color="#000000"/>
                <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"
                        ss:Color="#000000"/>
            </Borders>
            <Font ss:FontName="宋体" x:CharSet="134" ss:Size="12" ss:Color="#000000"
                  ss:Bold="1"/>
        </Style>
    </Styles>
    <Worksheet ss:Name="Sheet1">
        <Table ss:ExpandedColumnCount="1" ss:ExpandedRowCount="1" x:FullColumns="1"
               x:FullRows="1" ss:DefaultRowHeight="14.4">
        </Table>
        <#--<Table ss:ExpandedColumnCount="7" ss:ExpandedRowCount="${details?size+1}" x:FullColumns="1" x:FullRows="1"
               ss:DefaultColumnWidth="48" ss:DefaultRowHeight="14.4">
            <Column ss:Index="4" ss:StyleID="Default" ss:AutoFitWidth="0" ss:Width="72"/>
            <Column ss:StyleID="Default" ss:AutoFitWidth="0" ss:Width="36" ss:Span="2"/>
            <Row ss:Height="31.2">
                <Cell ss:StyleID="s50">
                    <Data ss:Type="String">楼栋</Data>
                </Cell>
                <Cell ss:StyleID="s50">
                    <Data ss:Type="String">楼层</Data>
                </Cell>
                <Cell ss:StyleID="s50">
                    <Data ss:Type="String">户名称</Data>
                </Cell>
                <Cell ss:StyleID="s50">
                    <Data ss:Type="String">户状态</Data>
                </Cell>
                <Cell ss:StyleID="s50">
                    <Data ss:Type="String">问题数</Data>
                </Cell>
                <Cell ss:StyleID="s50">
                    <Data ss:Type="String">整改数</Data>
                </Cell>
                <Cell ss:StyleID="s50">
                    <Data ss:Type="String">销项数</Data>
                </Cell>
            </Row>
            <#if details?exists && (details?size>0)>
                <#list details as item>
                    <Row>
                        <#if item.areaPathName?exists && (item.areaPathName?size>0)>
                            <#list item.areaPathName as apn>
                                <#if apn_index == 0>
                                    <Cell ss:StyleID="s51">
                                        <Data ss:Type="String">${apn}</Data>
                                    </Cell>
                                </#if>
                                <#if apn_index == 1>
                                    <Cell ss:StyleID="s51">
                                        <Data ss:Type="String">${apn}</Data>
                                    </Cell>
                                </#if>
                            </#list>
                        </#if>
                        <Cell ss:StyleID="s51">
                            <Data ss:Type="String">${item.areaName}</Data>
                        </Cell>
                        <Cell ss:StyleID="s51">
                            <Data ss:Type="String">${item.statusName}</Data>
                        </Cell>
                        <Cell ss:StyleID="s51">
                            <Data ss:Type="String">${item.issueCount}</Data>
                        </Cell>
                        <Cell ss:StyleID="s51">
                            <Data ss:Type="String">${item.issueRepairedCount}</Data>
                        </Cell>
                        <Cell ss:StyleID="s51">
                            <Data ss:Type="String">${item.issueApprovededCount}</Data>
                        </Cell>
                    </Row>
                </#list>
            </#if>
        </Table>-->
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
            </PageSetup>
            <Print>
                <ValidPrinterInfo/>
                <PaperSizeIndex>9</PaperSizeIndex>
                <HorizontalResolution>600</HorizontalResolution>
                <VerticalResolution>600</VerticalResolution>
            </Print>
            <Selected/>
            <Panes>
                <Pane>
                    <Number>3</Number>
                    <ActiveRow>4</ActiveRow>
                    <ActiveCol>2</ActiveCol>
                </Pane>
            </Panes>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
    <Worksheet ss:Name="Sheet2">
        <Table ss:ExpandedColumnCount="1" ss:ExpandedRowCount="1" x:FullColumns="1"
               x:FullRows="1" ss:DefaultRowHeight="14.4">
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
            </PageSetup>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
    <Worksheet ss:Name="Sheet3">
        <Table ss:ExpandedColumnCount="1" ss:ExpandedRowCount="1" x:FullColumns="1"
               x:FullRows="1" ss:DefaultRowHeight="14.4">
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
            </PageSetup>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>
</Workbook>
