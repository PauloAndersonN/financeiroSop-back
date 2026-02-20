SET SCHEMA FINANCEIRO;

-- itens
INSERT INTO item (id, nome, unidade) VALUES
  (1, 'Cimento CP-II', 'saco 50kg'),
  (2, 'Areia media', 'm3'),
  (3, 'Brita 1', 'm3'),
  (4, 'Tijolo ceramico', 'un'),
  (5, 'Cal hidratada', 'saco 20kg'),
  (6, 'Aco CA-50', 'barra'),
  (7, 'Bloco de concreto', 'un'),
  (8, 'Tinta acrilica', 'lata 18L'),
  (9, 'Tubulacao PVC 100mm', 'barra 6m'),
  (10, 'Brita 2', 'm3');

-- tipo_orcamento (predefinidos na documentacao)
MERGE INTO tipo_orcamento (nome) KEY(nome) VALUES
  ('Obra de Edificação'),
  ('Obra de Rodovias'),
  ('Outros');

-- orcamento
INSERT INTO orcamento (id, nup, tipo_orcamento_id, data_criacao, status, valor_total) VALUES
  (1, '20260000000000001', (SELECT id FROM tipo_orcamento WHERE nome = 'Obra de Edificação'), '2026-02-10 09:30:00', 'ABERTO', 24500.00),
  (2, '20260000000000002', (SELECT id FROM tipo_orcamento WHERE nome = 'Obra de Rodovias'), '2026-02-12 14:15:00', 'ABERTO', 9800.00),
  (3, '20260000000000003', (SELECT id FROM tipo_orcamento WHERE nome = 'Outros'), '2026-02-13 08:20:00', 'ABERTO', 15200.00),
  (4, '20260000000000004', (SELECT id FROM tipo_orcamento WHERE nome = 'Obra de Edificação'), '2026-02-14 16:40:00', 'FINALIZADO', 33250.00),
  (5, '20260000000000005', (SELECT id FROM tipo_orcamento WHERE nome = 'Obra de Rodovias'), '2026-02-16 11:05:00', 'ABERTO', 21400.00),
  (6, '20260000000000006', (SELECT id FROM tipo_orcamento WHERE nome = 'Outros'), '2026-02-17 10:10:00', 'ABERTO', 7600.00);

-- itens_orcamento
INSERT INTO item_orcamento (id, item_id, quantidade, valor_unitario, valor_total, quantidade_acumulada, orcamento_id) VALUES
  (1, 1, 100.00, 38.50, 3850.00, 20.00, 1),
  (2, 2, 30.00, 220.00, 6600.00, 10.00, 1),
  (3, 3, 20.00, 320.00, 6400.00, 0.00, 1),
  (4, 4, 2000.00, 1.95, 3900.00, 500.00, 2),
  (5, 5, 80.00, 22.50, 1800.00, 10.00, 2),
  (6, 6, 50.00, 45.00, 2250.00, 5.00, 3),
  (7, 7, 1200.00, 3.80, 4560.00, 200.00, 3),
  (8, 8, 60.00, 210.00, 12600.00, 20.00, 4),
  (9, 1, 200.00, 38.50, 7700.00, 150.00, 4),
  (10, 9, 40.00, 98.00, 3920.00, 8.00, 4),
  (11, 10, 25.00, 340.00, 8500.00, 0.00, 5),
  (12, 2, 40.00, 220.00, 8800.00, 12.00, 5),
  (13, 4, 1500.00, 1.95, 2925.00, 300.00, 6),
  (14, 5, 60.00, 22.50, 1350.00, 0.00, 6);

-- medicao
INSERT INTO medicao (id, numero, data_medicao, valor_medicao, status, observacao, orcamento_id) VALUES
  (1, 'MED-001', '2026-02-15', 2750.00, 'ABERTA', 'Primeira medicao do orcamento 1', 1),
  (2, 'MED-002', '2026-02-18', 1200.00, 'VALIDADA', 'Ajuste de quantidades', 1),
  (3, 'MED-003', '2026-02-16', 975.00, 'ABERTA', 'Medicao inicial do orcamento 2', 2),
  (4, 'MED-004', '2026-02-17', 2100.00, 'VALIDADA', 'Medicao parcial do orcamento 3', 3),
  (5, 'MED-005', '2026-02-18', 3250.00, 'ABERTA', 'Primeira medicao do orcamento 4', 4),
  (6, 'MED-006', '2026-02-19', 1800.00, 'ABERTA', 'Medicao inicial do orcamento 5', 5),
  (7, 'MED-007', '2026-02-19', 650.00, 'ABERTA', 'Medicao inicial do orcamento 6', 6);

-- item_medicao
INSERT INTO item_medicao (id, quantidade_medida, valor_total_medido, item_orcamento_id, medicao_id) VALUES
  (1, 10.00, 385.00, 1, 1),
  (2, 5.00, 1100.00, 2, 1),
  (3, 3.00, 115.50, 1, 2),
  (4, 2.00, 440.00, 2, 2),
  (5, 50.00, 97.50, 4, 3),
  (6, 8.00, 360.00, 6, 4),
  (7, 100.00, 380.00, 7, 4),
  (8, 12.00, 2520.00, 8, 5),
  (9, 20.00, 770.00, 9, 5),
  (10, 3.00, 294.00, 10, 5),
  (11, 4.00, 1360.00, 11, 6),
  (12, 6.00, 1320.00, 12, 6),
  (13, 100.00, 195.00, 13, 7),
  (14, 10.00, 225.00, 14, 7);
