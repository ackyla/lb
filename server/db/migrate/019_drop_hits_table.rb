class DropHitsTable < ActiveRecord::Migration
  def self.up
    drop_table :hits
  end

  def self.down
  end
end
