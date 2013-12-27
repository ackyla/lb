class DropResultsAndMissonsTables < ActiveRecord::Migration
  def self.up
    drop_table :results
    drop_table :missions
  end

  def self.down
  end
end
